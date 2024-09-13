package com.flixclusive.provider.basic_dummy_webview_provider

import android.content.Context
import com.flixclusive.model.provider.MediaLink
import com.flixclusive.model.tmdb.FilmDetails
import com.flixclusive.model.tmdb.common.tv.Episode
import com.flixclusive.provider.webview.ProviderWebView

class ProviderWebViewImpl(
    context: Context
) : ProviderWebView(context = context) {
    override val isHeadless: Boolean get() = false
    override val name: String get() = "My WebView Provider"

    override suspend fun getLinks(
        watchId: String,
        film: FilmDetails,
        episode: Episode?,
        onLinkFound: (MediaLink) -> Unit
    ) = throw NotImplementedError()
}