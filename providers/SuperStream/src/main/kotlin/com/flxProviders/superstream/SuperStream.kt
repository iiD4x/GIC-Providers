package com.flxProviders.superstream

import android.content.Context
import androidx.compose.runtime.Composable
import com.boooplay.provider.FlixclusiveProvider
import com.boooplay.provider.Provider
import com.boooplay.provider.ProviderApi
import com.flxProviders.superstream.api.SuperStreamApi
import com.flxProviders.superstream.api.settings.GetTokenScreen
import okhttp3.OkHttpClient

@FlixclusiveProvider
class SuperStream : Provider() {

    @Composable
    override fun SettingsScreen() {
        GetTokenScreen(settings = settings)
    }

    override fun getApi(
        context: Context,
        client: OkHttpClient
    ): ProviderApi {
        return SuperStreamApi(
            client = client,
            provider = this,
            settings = settings,
            context = context
        )
    }
}
