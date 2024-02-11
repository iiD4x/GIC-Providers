package com.github.yournamehere

import android.content.Context
import android.content.res.Resources
import androidx.compose.runtime.Composable
import com.flixclusive.provider.base.Provider
import com.flixclusive.provider.base.plugin.FlixclusivePlugin
import com.flixclusive.provider.base.plugin.Plugin
import okhttp3.OkHttpClient

/**
 * ## The main class for a Flixclusive plugin.
 *
 * #### WARN: Every plugin must be annotated with [FlixclusivePlugin].
 *
 * To create a plugin, extend this class and override the necessary methods.
 *
 * @see [TODO: ADD DOCS LINK FOR PLUGIN CREATION !!!!!]
 */
@FlixclusivePlugin
class MyFirstPlugin : Plugin() {

    /**
     * TIP: This is an optional method to override
     * if your provider won't require any settings screen.
     *
     *
     * Displays the settings screen for the plugin.
     *
     * @param resources Resources needed for rendering.
     */
    @Composable
    override fun SettingsScreen(resources: Resources?) {
        // Create a custom component for code readability
        // ExampleSettingsScreen(resources = resources)


        // TODO("OPTIONAL: Not yet implemented")
    }

    /**
     * TIP: This is an optional function to override.
     *
     * It loads this plugin from the app.
     *
     *
     * #### WARN: Don't remove the super call as it will load the plugin to the app.
     *
     *
     * To run a block of code *before* loading, just simply put the `super.load(context)` at the end of this method's call.
     * ```
     * override fun load(context: Context?) {
     *      // ... run whatever you want before loading.
     *      super.load(context)
     * }
     * ```
     * It's the same as well with running a block of code *after* loading. Just put the `super.load(context)` at the beginning of this method's call.
     * ```
     * override fun load(context: Context?) {
     *      super.load(context)
     *      // ... run whatever you want after loading.
     * }
     * ```
     *
     * @param context The [Context] of the app.
     */
    override fun load(
        context: Context?,
        client: OkHttpClient
    ) {
        super.load(context, client)
        // TODO("OPTIONAL: Not yet implemented")
    }

    /**
     * TIP: This is an optional function to override. 
     * 
     * It unloads this plugin from the app.
     * 
     * 
     * #### WARN: Don't remove the super call as it will load the plugin to the app.
     * 
     * 
     * To run a block of code *before* unloading, just simply put the `super.unload(context)` at the end of this method's call.
     * ```
     * override fun unload(context: Context?) {
     *      // ... run whatever you want before unloading.
     *      super.unload(context)
     * }
     * ```
     * It's the same as well with running a block of code *after* unloading. Just put the `super.unload(context)` at the beginning of this method's call.
     * ```
     * override fun unload(context: Context?) {
     *      super.unload(context)
     *      // ... run whatever you want after unloading.
     * }
     * ```
     *
     * @param context The [Context] of the app.
     */
    override fun unload(context: Context?) {
        super.unload(context)
        // TODO("OPTIONAL: Not yet implemented")
    }
}
