package com.voxyl.overlay.config

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