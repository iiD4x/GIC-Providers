package com.flxProviders.sudoflix

import android.content.Context
import com.boooplay.provider.FlixclusiveProvider
import com.boooplay.provider.Provider
import com.boooplay.provider.ProviderApi
import com.flxProviders.sudoflix.api.SudoFlixApi
import okhttp3.OkHttpClient

@FlixclusiveProvider
class SudoFlix : Provider() {
    override fun getApi(
        context: Context,
        client: OkHttpClient
    ): ProviderApi {
        return SudoFlixApi(
            client = client,
            provider = this
        )
    }
}
