package com.flxProviders.sudoflix.api

import com.boooplay.core.util.coroutines.mapAsync
import com.boooplay.core.util.exception.safeCall
import com.boooplay.model.film.util.FilmType
import com.boooplay.provider.filter.FilterList
import com.boooplay.model.provider.link.MediaLink
import com.boooplay.model.film.FilmDetails
import com.boooplay.model.film.FilmSearchItem
import com.boooplay.model.film.Movie
import com.boooplay.model.film.SearchResponseData
import com.boooplay.model.film.common.tv.Episode
import com.boooplay.provider.Provider
import com.boooplay.provider.ProviderApi
import com.flxProviders.sudoflix.api.nsbx.NsbxApi
import com.flxProviders.sudoflix.api.nsbx.VidBingeApi
import com.flxProviders.sudoflix.api.primewire.PrimeWireApi
import com.flxProviders.sudoflix.api.ridomovies.RidoMoviesApi
import com.flxProviders.sudoflix.api.vidsrcto.VidSrcToApi
import okhttp3.OkHttpClient

/**
 *
 * RIP m-w
 * */
class SudoFlixApi(
    client: OkHttpClient,
    provider: Provider
) : ProviderApi(
    client = client,
    provider = provider
) {
    private val providersList = listOf(
        NsbxApi(
            client = client,
            provider = provider
        ),
        VidBingeApi(
            client = client,
            provider = provider
        ),
        RidoMoviesApi(
            client = client,
            provider = provider
        ),
        PrimeWireApi(
            client = client,
            provider = provider
        ),
        VidSrcToApi(
            client = client,
            provider = provider
        ),
    )

    override val testFilm: FilmDetails
        get() = Movie(
            tmdbId = 299534,
            imdbId = "tt4154796",
            title = "Avengers: Endgame",
            posterImage = null,
            backdropImage = "/orjiB3oUIsyz60hoEqkiGpy5CeO.jpg",
            homePage = null,
            id = null,
            providerName = "TMDB"
        )

    override suspend fun getLinks(
        watchId: String,
        film: FilmDetails,
        episode: Episode?,
        onLinkFound: (MediaLink) -> Unit
    ) {
        providersList.mapAsync {
            safeCall {
                it.getLinks(
                    watchId = watchId,
                    film = film,
                    episode = episode,
                    onLinkFound = onLinkFound
                )
            }
        }
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
        if (identifier == null) {
            throw IllegalStateException("${provider.name} is not a searchable provider. It is a set of providers combined into one.")
        }

        return SearchResponseData(
            results = listOf(
                FilmSearchItem(
                    id = identifier,
                    title = title,
                    providerName = provider.name,
                    filmType = FilmType.MOVIE,
                    posterImage = null,
                    backdropImage = null,
                    homePage = null
                )
            )
        )
    }
}