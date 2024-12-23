@file:Suppress("SpellCheckingInspection")

package com.flxProviders.stremio.api

import androidx.compose.ui.util.fastDistinctBy
import androidx.compose.ui.util.fastFlatMap
import androidx.compose.ui.util.fastForEach
import com.boooplay.core.util.coroutines.asyncCalls
import com.boooplay.core.util.coroutines.mapAsync
import com.boooplay.core.util.exception.safeCall
import com.boooplay.core.util.network.json.fromJson
import com.boooplay.core.util.network.okhttp.request
import com.boooplay.model.film.Film
import com.boooplay.model.film.FilmDetails
import com.boooplay.model.film.FilmSearchItem
import com.boooplay.model.film.Movie
import com.boooplay.model.film.SearchResponseData
import com.boooplay.model.film.common.tv.Episode
import com.boooplay.model.film.util.FilmType
import com.boooplay.model.provider.ProviderCatalog
import com.boooplay.model.provider.link.MediaLink
import com.boooplay.model.provider.link.Stream
import com.boooplay.provider.Provider
import com.boooplay.provider.ProviderApi
import com.boooplay.provider.filter.FilterList
import com.boooplay.provider.settings.ProviderSettings
import com.flxProviders.stremio.api.model.Addon
import com.flxProviders.stremio.api.model.Catalog
import com.flxProviders.stremio.api.model.EMBEDDED_IMDB_ID_KEY
import com.flxProviders.stremio.api.model.EMBEDDED_STREAM_KEY
import com.flxProviders.stremio.api.model.FetchCatalogResponse
import com.flxProviders.stremio.api.model.FetchMetaResponse
import com.flxProviders.stremio.api.model.StreamDto.Companion.toStreamLink
import com.flxProviders.stremio.api.model.StreamResponse
import com.flxProviders.stremio.api.model.toFilmDetails
import com.flxProviders.stremio.api.model.toFilmSearchItem
import com.flxProviders.stremio.api.util.OpenSubtitlesUtil.fetchSubtitles
import com.flxProviders.stremio.api.util.isValidUrl
import com.flxProviders.stremio.settings.util.AddonUtil.DEFAULT_META_PROVIDER
import com.flxProviders.stremio.settings.util.AddonUtil.DEFAULT_META_PROVIDER_BASE_URL
import com.flxProviders.stremio.settings.util.AddonUtil.getAddons
import okhttp3.OkHttpClient

internal const val STREAMIO_ADDONS_KEY = "streamio_addons"
internal const val ADDON_SOURCE_KEY = "addonSource"
internal const val MEDIA_TYPE_KEY = "type"
internal const val STREMIO = "Stremio"

internal class StremioApi(
    provider: Provider,
    client: OkHttpClient,
    private val settings: ProviderSettings
) : ProviderApi(
    client = client,
    provider = provider
) {
    private val name = STREMIO

    override val testFilm: FilmDetails
        get() = (super.testFilm as Movie).copy(
            providerName = name,
        )

    override val catalogs: List<ProviderCatalog>
        get() = safeCall {
            settings.getAddons().fastFlatMap { addon ->
                addon.homeCatalogs
            }
        } ?: emptyList()

    override suspend fun getCatalogItems(
        catalog: ProviderCatalog,
        page: Int
    ): SearchResponseData<FilmSearchItem> {
        val catalogProperties = fromJson<Catalog>(catalog.url)
        val addonSource = catalogProperties.addonSource
        val addon = getAddonByName(name = addonSource)

        val query = catalogProperties.getCatalogQuery(page = page)

        val failedFetchErrorMessage = "[${catalog.name}]> Coudn't fetch catalog items"
        val response = client.request(url = "${addon.baseUrl}/$query")
            .execute()
            .fromJson<FetchCatalogResponse>(errorMessage = failedFetchErrorMessage)

        if (response.err != null) {
            throw Exception(failedFetchErrorMessage)
        }

        val results = mutableListOf<FilmSearchItem>()
        response.items?.fastForEach {
            val item = it.toFilmSearchItem(addonName = addon.name)
            results.add(item)
        }

        return SearchResponseData(
            page = page,
            results = results,
            hasNextPage = results.size != 0,
        )
    }

    override suspend fun getLinks(
        watchId: String,
        film: FilmDetails,
        episode: Episode?,
        onLinkFound: (MediaLink) -> Unit
    ) {
        asyncCalls(
            {
                film.getLinks(
                    watchId = watchId,
                    episode = episode,
                    onLinkFound = onLinkFound
                )
            },
            {
                val imdbId = film.customProperties[EMBEDDED_IMDB_ID_KEY]
                    ?: film.imdbId
                    ?: film.identifier

                client.fetchSubtitles(
                    imdbId = imdbId,
                    season = episode?.season,
                    episode = episode?.number,
                    onLinkFound = onLinkFound
                )
            },
        )
    }

    override suspend fun search(
        title: String,
        page: Int,
        id: String?,
        imdbId: String?,
        tmdbId: Int?,
        filters: FilterList,
    ): SearchResponseData<FilmSearchItem> {
        val identifier = id ?: tmdbId?.toString() ?: imdbId
        if (identifier != null) {
            return SearchResponseData(
                results = listOf(
                    FilmSearchItem(
                        id = identifier,
                        title = title,
                        providerName = name,
                        posterImage = null,
                        backdropImage = null,
                        homePage = null,
                        filmType = FilmType.MOVIE
                    )
                )
            )
        }

        val results = mutableListOf<FilmSearchItem>()
        settings.getAddons().mapAsync { addon ->
            addon.searchableCatalogs.mapAsync { catalog ->
                val query = catalog.getCatalogQuery(
                    searchQuery = title,
                    page = page,
                )

                val items = safeCall {
                    val baseUrl = when (catalog.addonSource) {
                        DEFAULT_META_PROVIDER -> DEFAULT_META_PROVIDER_BASE_URL
                        else -> addon.baseUrl
                    }

                    client.request(url = "$baseUrl/$query")
                        .execute()
                        .fromJson<FetchCatalogResponse>()
                        .items?.mapAsync {
                            it.toFilmSearchItem(addonName = addon.name)
                        }
                } ?: emptyList()

                results.addAll(items)
            }
        }

        return SearchResponseData(
            results = results.fastDistinctBy { it.identifier }.toList(),
            page = page,
            hasNextPage = results.size >= 20
        )
    }

    override suspend fun getFilmDetails(film: Film): FilmDetails {
        val nameKey = "${film.id}=${film.title}"

        val addonSource = film.customProperties[ADDON_SOURCE_KEY]
            ?: throw IllegalArgumentException("[$nameKey]> Addon source must not be null!")
        val type = film.customProperties[MEDIA_TYPE_KEY]
            ?: throw IllegalArgumentException("[$nameKey]> Media type must not be null!")

        val addon = getAddonByName(name = addonSource)
        if (!addon.hasMeta && film.hasImdbId) {
            return getFilmDetailsFromDefaultMetaProvider(
                id = film.imdbId!!,
                type = type,
                nameKey = nameKey
            )
        } else if (!addon.hasMeta)
            throw IllegalArgumentException("[${addonSource}]> Addon has no metadata resource!")

        val url = "${addon.baseUrl}/meta/$type/${film.identifier}.json"
        return getFilmDetails(
            addonName = addonSource,
            nameKey = nameKey,
            url = url
        )
    }

    private fun getFilmDetails(
        addonName: String,
        nameKey: String,
        url: String
    ): FilmDetails {
        val failedToFetchMetaDataMessage = "[${addonName}]> Coudn't fetch meta data of $nameKey ($url)"
        val response = client.request(url = url).execute()
            .fromJson<FetchMetaResponse>(errorMessage = failedToFetchMetaDataMessage)

        if (response.err != null) {
            throw Exception(failedToFetchMetaDataMessage)
        }

        return response.film?.toFilmDetails(addonName)
            ?: throw IllegalArgumentException("[$nameKey]> Meta data is null!")
    }

    private suspend fun FilmDetails.getLinks(
        watchId: String,
        episode: Episode?,
        onLinkFound: (MediaLink) -> Unit
    ) {
        val embeddedStream = customProperties[EMBEDDED_STREAM_KEY]
        if (embeddedStream != null) {
            val stream = Stream(
                name = title,
                url = embeddedStream
            )

            return onLinkFound(stream)
        }

        val addons = settings.getAddons()
        val streamType = customProperties[MEDIA_TYPE_KEY]
        val addonSourceName = customProperties[ADDON_SOURCE_KEY]
        val isFromStremio = providerName.equals(STREMIO, true)
        val id = if (isFromStremio) watchId else imdbId
            ?: throw IllegalArgumentException("[$id]> IMDB ID should not be null!")

        if (addonSourceName != null) {
            val addonSource = getAddonByName(name = addonSourceName)
            if (addonSource.hasStream) {
                addonSource.getStream(
                    watchId = id,
                    filmType = filmType,
                    streamType = streamType,
                    episode = episode,
                    isFromStremio = isFromStremio,
                    onLinkFound = onLinkFound
                )
            }
        }

        addons.mapAsync { addon ->
            if (!addon.hasStream)
                return@mapAsync

            addon.getStream(
                watchId = id,
                filmType = filmType,
                streamType = streamType,
                episode = episode,
                isFromStremio = isFromStremio,
                onLinkFound = onLinkFound
            )
        }
    }

    private fun Addon.getStream(
        watchId: String,
        filmType: FilmType,
        isFromStremio: Boolean,
        streamType: String?,
        episode: Episode?,
        onLinkFound: (MediaLink) -> Unit
    ) {
        val hasStreamUrlOnEpisode = episode != null && isValidUrl(episode.id)
        if (hasStreamUrlOnEpisode) {
            val stream = Stream(
                name = episode!!.title,
                url = episode.id
            )

            return onLinkFound(stream)
        }

        val query = when (streamType) {
            null -> getStreamQuery(
                id = watchId,
                type = filmType,
                isFromStremio = isFromStremio,
                episode = episode
            )
            else -> getStreamQuery(
                id = watchId,
                type = streamType,
                isFromStremio = isFromStremio,
                episode = episode
            )
        }

        val response = client.request(
            url = "$baseUrl/$query"
        ).execute()
            .fromJson<StreamResponse>()

        if (response.err != null)
            return

        response.streams.forEach { stream ->
            val sourceLink = stream.toStreamLink()
            if (sourceLink != null)
                onLinkFound(sourceLink)

            stream.subtitles?.forEach sub@ { subtitle ->
                val isValidUrl = isValidUrl(subtitle.url)
                if (!isValidUrl) return@sub

                onLinkFound(subtitle)
            }
        }
    }

    private fun getFilmDetailsFromDefaultMetaProvider(
        id: String,
        type: String,
        nameKey: String
    ): FilmDetails {
        val url = "$DEFAULT_META_PROVIDER_BASE_URL/meta/$type/$id.json"

        return getFilmDetails(
            addonName = DEFAULT_META_PROVIDER,
            nameKey = nameKey,
            url = url
        )
    }

    private fun getAddonByName(name: String): Addon {
        return settings.getAddons()
            .firstOrNull { it.isCorrectSource(name) }
            ?: throw IllegalArgumentException("[${name}]> Addon cannot be found")
    }

    private fun Addon.isCorrectSource(name: String)
            = this.name.equals(name, true)

    private val Film.hasImdbId: Boolean
        get() = imdbId != null || id?.startsWith("tt") == true
}