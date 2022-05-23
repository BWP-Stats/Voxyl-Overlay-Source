package com.voxyl.overlay.settings

import com.voxyl.overlay.settings.config.Config
import com.voxyl.overlay.settings.window.SavedWindowState
import com.voxyl.overlay.kindasortasomewhatviewmodelsishiguessithinkidkwhatevericantbebotheredsmh.StatsToShow
import com.voxyl.overlay.settings.misc.MiscSettings

object Settings {

    private val root = System.getenv("APPDATA")

    fun storeAll(
        configPath: String = "$root/.voverlay/config.properties",
        savedWindowStatePath: String = "$root/.voverlay/window-state.properties",
        miscPath: String = "$root/.voverlay/misc.properties"
    ) {
        StatsToShow.save()
        Config.store(configPath)
        SavedWindowState.store(savedWindowStatePath)
        MiscSettings.store(miscPath)
    }
}