package com.voxyl.overlay.settings

import com.voxyl.overlay.settings.config.Config
import com.voxyl.overlay.settings.window.SavedWindowState

object Settings {

    private val defaultPath = System.getenv("APPDATA")

    fun storeAll(
        settingsPath: String = "$defaultPath/.voverlay/config.properties",
        savedWindowStatePath: String = "$defaultPath/.voverlay/window-state.properties",
    ) {
        Config.store(settingsPath)
        SavedWindowState.store(savedWindowStatePath)
    }
}