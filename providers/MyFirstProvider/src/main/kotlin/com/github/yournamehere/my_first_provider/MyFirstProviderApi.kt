package com.github.yournamehere.my_first_provider

import android.content.Context
import com.flixclusive.model.provider.ProviderCatalog
import com.flixclusive.model.provider.SourceLink
import com.flixclusive.model.provider.Subtitle
import com.flixclusive.model.tmdb.Film
import com.flixclusive.model.tmdb.FilmDetails
import com.flixclusive.model.tmdb.FilmSearchItem
import com.flixclusive.model.tmdb.Movie
import com.flixclusive.model.tmdb.SearchResponseData
import com.flixclusive.model.tmdb.TvShow
import com.flixclusive.model.tmdb.common.tv.Episode
import com.flixclusive.provider.Provider
import com.flixclusive.provider.ProviderApi
import com.flixclusive.provider.util.FlixclusiveWebView
import com.flixclusive.provider.util.WebViewCallback
import okhttp3.OkHttpClient

/**
 * An inheritance class for a [ProviderApi]. This will serve as the [Provider] api instance.
 *
 */
class MyFirstProviderApi(
    client: OkHttpClient
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
     * Whether this provider needs to use a WebView to scrape content
     * */
    override val useWebView: Boolean
        get() = super.useWebView

    /**
     *
     * The list of [ProviderCatalog] that this provider supports. Defaults to empty
     * */
    override val catalogs: List<ProviderCatalog>
        get() = super.catalogs

    /**
     *
     * Called when the app needs to fetch items from
     * this provider's [catalogs] list.
     * */
    override suspend fun getCatalogItems(
        catalog: ProviderCatalog,
        page: Int
    ): SearchResponseData<FilmSearchItem> {
        return super.getCatalogItems(catalog, page)
    }

    /**
     * Retrieves detailed information about a film.
     * @param film The Film object of the film to retrieve details for.
     * @return a [FilmDetails] instance containing the film's information.
     */
    override suspend fun getFilmDetails(film: Film): FilmDetails {
        TODO("Not yet implemented")
    }

    /**
     * Obtains source links for the provided film, season, and episode.
     * @param watchId The ID of the film. The ID must come from the [search] method.
     * @param film The [Film] object of the film. It could either be a [Movie] or [TvShow].
     * @param episode The episode number. Defaults to null if the film is a movie.
     * @param onLinkLoaded A callback function invoked when a [SourceLink] is loaded.
     * @param onSubtitleLoaded A callback function invoked when a [Subtitle] is loaded.
     */
    override suspend fun getSourceLinks(
        watchId: String,
        film: FilmDetails,
        episode: Episode?,
        onLinkLoaded: (SourceLink) -> Unit,
        onSubtitleLoaded: (Subtitle) -> Unit
    ) {
        TODO("Not yet implemented")
    }

    /**
     * Searches for films based on the provided criteria.
     *
     * @param title The title of the film to search for.
     * @param id The ID of the film to search for (optional).
     * @param tmdbId The TMDB ID of the film to search for (optional).
     * @param imdbId The IMDB ID of the film to search for (optional).
     * @param page The page number of the search results (optional, defaults to 1).
     *
     * @return A [SearchResponseData] object containing the search results.
     */
    override suspend fun search(
        title: String,
        page: Int,
        id: String?,
        imdbId: String?,
        tmdbId: Int?
    ): SearchResponseData<FilmSearchItem> {
        return super.search(title, page, id, imdbId, tmdbId)
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
        film: FilmDetails,
        episode: Episode?
    ): FlixclusiveWebView? {
        return super.getWebView(context, callback, film, episode)
    }
}