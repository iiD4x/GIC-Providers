import com.boooplay.model.provider.Language
import com.boooplay.model.provider.ProviderType
import com.boooplay.model.provider.Status

dependencies {
    implementation("androidx.core:core:1.13.1")
    /**
     * Custom dependencies for each provider should be implemented here.
     * */
    // implementation( ... )

    // Comment if not implementing own SettingsScreen
    // No need to specify the compose version explicitly
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.runtime:runtime")

    implementation("io.coil-kt:coil-compose:2.5.0")
    // ================= END: COMPOSE UI =================

}

android {
    buildFeatures {
        compose = true
    }

    defaultConfig {
        vectorDrawables.useSupportLibrary = false
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
}

flxProvider {
    description.set("""
        A flixclusive adapter for Stremio addons. Torrent addons, such as Torrentio, don't work without debrid accounts.
    """.trimIndent())

    changelog.set("""
        ### Fixes:
        - update compose BOM to 2024.09.02
    """.trimIndent())

    versionMajor = 1
    versionMinor = 2
    versionPatch = 3
    versionBuild = 2

    // Extra authors for specific provider
    // author(
    //    name = "...",
    //    githubLink = "https://github.com/...",
    // )
    // ===

    iconUrl.set("https://i.imgur.com/Hoq93zL.png") // OPTIONAL

    language.set(Language.Multiple)

    providerType.set(ProviderType.All)

    status.set(Status.Working)

    requiresResources.set(true)
}

