package com.voxyl.overlay.settings

import com.voxyl.overlay.settings.config.Config
import com.voxyl.overlay.settings.window.SavedWindowState

object Settings {

    private val root = System.getenv("APPDATA")

    fun storeAll(
        settingsPath: String = "$root/.voverlay/config.properties",
        savedWindowStatePath: String = "$root/.voverlay/window-state.properties",
    ) {
        Config.store(settingsPath)
        SavedWindowState.store(savedWindowStatePath)
    }
}