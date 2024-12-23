package com.flxProviders.sudoflix.api.ridomovies.extractor

import com.boooplay.core.util.coroutines.mapAsync
import com.boooplay.core.util.network.Crypto
import com.boooplay.core.util.network.jsoup.asJsoup
import com.boooplay.core.util.network.okhttp.request
import com.boooplay.model.provider.link.Flag
import com.boooplay.model.provider.link.MediaLink
import com.boooplay.model.provider.link.Stream
import com.boooplay.model.provider.link.Subtitle
import com.boooplay.model.provider.link.SubtitleSource
import com.boooplay.provider.extractor.EmbedExtractor
import com.flxProviders.sudoflix.api.ridomovies.RidoMoviesConstant.RIDO_MOVIES_BASE_URL
import com.flxProviders.sudoflix.api.util.JsUnpacker
import okhttp3.Headers.Companion.toHeaders
import okhttp3.OkHttpClient

internal class CloseLoad(
    client: OkHttpClient
) : EmbedExtractor(client) {
    override val baseUrl = "https://closeload.top"
    override val name = "CloseLoad"

    private val headers = mapOf(
        "Referer" to "$RIDO_MOVIES_BASE_URL/"
    ).toHeaders()

    override suspend fun extract(
        url: String,
        customHeaders: Map<String, String>?,
        onLinkFound: (MediaLink) -> Unit
    ) {
        val links = mutableListOf<MediaLink>()

        val response = client.request(
            url = url,
            headers = headers
        ).execute()

        if (!response.isSuccessful || response.body == null) {
            throw IllegalStateException("Failed to fetch page [$name]")
        }

        val htmlCode = response.asJsoup()

        val captions = htmlCode.select("track")

        captions.mapAsync {
            links.add(
                Subtitle(
                    language = "[$name] ${it.attr(" label ")}",
                    url = baseUrl+it.attr("src"),
                    type = SubtitleSource.ONLINE
                )
            )
        }

        val packedScript = htmlCode.selectFirst("script:containsData(function(p,a,c,k,e,d))")
            ?.data()
            ?: throw IllegalStateException("[$name]> Could not find eval script")

        val unpacked = JsUnpacker(packedScript).unpack()
            ?: throw NullPointerException("[$name]> Unable to find source url")

        val regexPattern = """var\s+(\w+)\s*=\s*"([^"]+)";""".toRegex()
        val matchResult = regexPattern.find(unpacked)
        val base64EncodedUrl = matchResult?.groups?.get(2)?.value
            ?: throw NullPointerException("[$name]> Unable to find source url")

        val sourceUrl = Crypto.base64Decode(base64EncodedUrl)

        onLinkFound(
            Stream(
                url = sourceUrl,
                name = "[$name]> HLS",
                flags = setOf(
                    Flag.RequiresAuth(
                        customHeaders = mapOf("Referer" to "$baseUrl/")
                    )
                )
            )
        )
    }
}