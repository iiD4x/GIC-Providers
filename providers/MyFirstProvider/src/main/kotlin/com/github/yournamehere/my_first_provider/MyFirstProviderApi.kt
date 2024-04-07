package com.github.yournamehere.my_first_provider

import android.content.Context
import com.flixclusive.core.util.film.FilmType
import com.flixclusive.model.provider.SourceLink
import com.flixclusive.model.provider.Subtitle
import com.flixclusive.model.tmdb.Film
import com.flixclusive.model.tmdb.Movie
import com.flixclusive.model.tmdb.TMDBEpisode
import com.flixclusive.model.tmdb.TvShow
import com.flixclusive.provider.Provider
import com.flixclusive.provider.ProviderApi
import com.flixclusive.provider.dto.FilmInfo
import com.flixclusive.provider.dto.SearchResults
import com.flixclusive.provider.extractor.Extractor
import com.flixclusive.provider.util.FlixclusiveWebView
import com.flixclusive.provider.util.WebViewCallback
import okhttp3.OkHttpClient

/**
 * An inheritance class for a [ProviderApi]. This will serve as the [Provider] api instance.
 *
 * #### NOTE: Always add these template parameters on the constructor. The app will handle this automatically.
 *
 * @param client The [OkHttpClient] instance used for network requests.
 * @property provider The [Provider] instance that contains all the provider information of this api. You can change the data type of [Provider] to your custom [Provider] if you'd like. For example:
 * ```
 * class MyFirstProviderApi(
 *     ...,
 *     private val provider: MyFirstProvider
 * ) : ProviderApi(client) {
 * ```
 */
class MyFirstProviderApi(
    client: OkHttpClient,
    private val provider: Provider
) : ProviderApi(client) {
    /**
     * The name of the provider.
     */
    override val name: String
        get() = "MyFirstProviderApi"

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
     * Whether this provider needs to use a WebView to scrape content
     * */
    override val useWebView: Boolean
        get() = super.useWebView

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
     * @param filmId The ID of the film. The ID must come from the [search] method.
     * @param film The [Film] object of the film. It could either be a [Movie] or [TvShow].
     * @param episode The episode number. Defaults to null if the film is a movie.
     * @param onLinkLoaded A callback function invoked when a [SourceLink] is loaded.
     * @param onSubtitleLoaded A callback function invoked when a [Subtitle] is loaded.
     */
    override suspend fun getSourceLinks(
        filmId: String,
        film: Film,
        season: Int?,
        episode: Int?,
        onLinkLoaded: (SourceLink) -> Unit,
        onSubtitleLoaded: (Subtitle) -> Unit
    ) {
        TODO("Not yet implemented")
    }

    /**
     * Performs a search for films based on the provided query.
     * @param film The [Film] object of the film. It could either be a [Movie] or [TvShow].
     * @param page The page number for paginated results. Defaults to 1.
     * @return a [SearchResults] instance containing the search results.
     */
    override suspend fun search(
        film: Film,
        page: Int
    ): SearchResults {
        TODO("Not yet implemented")
    }

    /**
     * Creates a new FlixclusiveWebView instance for the given film and episode.
     *
     * @param context The Android context.
     * @param callback The [WebViewCallback] to handle events from the WebView.
     * @param film The film to play.
     * @param episode The episode to play, if it is a tv show.
     * @return A new [FlixclusiveWebView] instance, or null if this provider won't use any WebViews.
     */
    override fun getWebView(
        context: Context,
        callback: WebViewCallback,
        film: Film,
        episode: TMDBEpisode?
    ): FlixclusiveWebView? = super.getWebView(context, callback, film, episode)
}