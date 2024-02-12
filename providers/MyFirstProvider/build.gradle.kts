import com.flixclusive.gradle.entities.Language
import com.flixclusive.gradle.entities.PluginType
import com.flixclusive.gradle.entities.Status

/**
 *
 * Versions by level/hierarchy.
 * Increment one of these to trigger the updater
 * */
val versionMajor = 1
val versionMinor = 0
val versionPatch = 0

// !! WARN: PLEASE DO NOT CHANGE HOW THE VERSION NAMING IS STRUCTURED !!
version = "${versionMajor}.${versionMinor}.${versionPatch}"


description = "My first provider!" // Plugin description that will be shown to user


dependencies {
    /**
     * Custom dependencies for each provider should fall here.
     * */
    // implementation( ... )
}

flixclusive {
    // ====== Provider Description =======
    // Changelog of your plugin
    changelog.set(
        """
        Some changelog:
        
        TODO: Add your changes here...
        """.trimIndent()
    ) // OPTIONAL
    /**
     * Image or Gif that will be shown at the top of your changelog page
     * */
    // changelogMedia.set("https://cool.png") // OPTIONAL

    /**
     * Add additional authors to this plugin
     * */
    author("SecondAuthor", discordId = 123L)
    // author( ... )

    /**
     * If your provider has an icon, put its image url here.
     * */
    // iconUrl.set("https://cool.png") // OPTIONAL

    /**
     * The main language of your provider.
     *
     * There are two supported values:
     * - Language.Multiple
     *      > Obviously for providers w/ multiple language support.
     * - Language.Specific("en")
     *      > For specific languages only. NOTE: Use the language's short-hand code.
     */
    language.set(Language.Multiple)

    /**
     * The main type that your provider supports.
     *
     * These are the possible values you could set:
     * - PluginType.All
     * - PluginType.TvShows
     * - PluginType.Movies
     * - PluginType.Custom(customType: String) // i.e., PluginType.Custom("OVA")
     */
    pluginType.set(PluginType.All)

    /**
     * The current status of this provider.
     *
     * These are the possible values you could set:
     * - Status.Beta
     * - Status.Maintenance
     * - Status.Down
     * - Status.Working
     */
    status.set(Status.Working)
    // ================


    // === Utilities ===
    /**
     * Toggle this if you want to use the resources (R) of the main application.
     */
    // requiresResources.set(true) // OPTIONAL

    /**
     * Excludes this plugin from the updater,
     * meaning it won't show up for users.
     * Set this if the plugin is unfinished
     */
    // excludeFromUpdaterJson.set(true) // OPTIONAL
    // =================
}

