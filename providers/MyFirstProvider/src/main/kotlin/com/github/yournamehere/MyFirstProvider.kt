package com.github.yournamehere

import com.flixclusive.core.util.film.FilmType
import com.flixclusive.model.provider.SourceLink
import com.flixclusive.model.provider.Subtitle
import com.flixclusive.provider.base.Provider
import com.flixclusive.provider.base.dto.FilmInfo
import com.flixclusive.provider.base.dto.SearchResults
import okhttp3.OkHttpClient

// You can use client.request() for calling OkHttp requests
class MyFirstProvider(
    /**
     *
     * Always add OkHttpClient on the constructor. The app will handle this automatically
     * */
    client: OkHttpClient
) : Provider(client) {
    override val name: String
        get() = TODO("Not yet implemented")

    override suspend fun getFilmInfo(filmId: String, filmType: FilmType): FilmInfo {
        TODO("Not yet implemented")
    }

    override suspend fun getSourceLinks(
        filmId: String,
        season: Int?,
        episode: Int?,
        onLinkLoaded: (SourceLink) -> Unit,
        onSubtitleLoaded: (Subtitle) -> Unit
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun search(query: String, page: Int, filmType: FilmType): SearchResults {
        TODO("Not yet implemented")
    }
}