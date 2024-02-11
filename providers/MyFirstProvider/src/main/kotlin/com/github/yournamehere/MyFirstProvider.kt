package com.github.yournamehere

import com.flixclusive.core.util.film.FilmType
import com.flixclusive.model.provider.SourceLink
import com.flixclusive.model.provider.Subtitle
import com.flixclusive.provider.base.Provider
import com.flixclusive.provider.base.dto.FilmInfo
import com.flixclusive.provider.base.dto.SearchResults
import com.flixclusive.provider.base.extractor.Extractor
import com.flixclusive.provider.base.plugin.Plugin
import okhttp3.OkHttpClient

/**
 * An inheritance class for a [Provider]. This will serve as your own [Provider] instance.
 *
 * NOTE: Always add these template parameters on the constructor. The app will handle this automatically.
 *
 * @param client The [OkHttpClient] instance used for network requests.
 * @param plugin The [Plugin] instance that contains all the plugin information of this provider. You could change the data type of [Plugin] to your custom [Plugin] if you'd like. For example, plugin: [MyFirstPlugin]
 */
class MyFirstProvider(
    client: OkHttpClient,
    plugin: Plugin
) : Provider(client) {
    /**
     * The name of the provider.
     */
    override val name: String
        get() = TODO("Not yet implemented")

    /**
     * The base URL used for network requests. Defaults to an empty string.
     */
    override val baseUrl: String
        get() = super.baseUrl

    /**
     * The list of supported extractors, embeds, or servers for this provider instance. Defaults to an empty list
     */
    override val supportedExtractors: List<Extractor>
        get() = super.supportedExtractors

    /**
     * Retrieves detailed information about a film.
     * @param filmId The ID of the film.
     * @param filmType The type of film.
     * @return a [FilmInfo] instance containing the film's information.
     */
    override suspend fun getFilmInfo(
        filmId: String,
        filmType: FilmType
    ): FilmInfo {
        TODO("Not yet implemented")
    }

    /**
     * Obtains source links for the provided film, season, and episode.
     * @param filmId The ID of the film.
     * @param season The season number. Defaults to null if the film is a movie.
     * @param episode The episode number. Defaults to null if the film is a movie.
     * @param onLinkLoaded A callback function invoked when a [SourceLink] is loaded.
     * @param onSubtitleLoaded A callback function invoked when a [Subtitle] is loaded.
     */
    override suspend fun getSourceLinks(
        filmId: String,
        season: Int?,
        episode: Int?,
        onLinkLoaded: (SourceLink) -> Unit,
        onSubtitleLoaded: (Subtitle) -> Unit
    ) {
        TODO("Not yet implemented")
    }

    /**
     * Performs a search for films based on the provided query.
     * @param query The search query.
     * @param page The page number for paginated results. Defaults to 1.
     * @param filmType The type of film being searched for.
     * @return a [SearchResults] instance containing the search results.
     */
    override suspend fun search(
        query: String,
        page: Int,
        filmType: FilmType
    ): SearchResults {
        TODO("Not yet implemented")
    }
}