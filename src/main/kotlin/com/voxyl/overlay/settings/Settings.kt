package com.voxyl.overlay.settings

import com.voxyl.overlay.settings.config.Config
import com.voxyl.overlay.settings.window.SavedWindowState
import com.voxyl.overlay.kindasortasomewhatviewmodelsishiguessithinkidkwhatevericantbebotheredsmh.StatsToShow
import com.voxyl.overlay.settings.misc.MiscKeys.FirstTime
import com.voxyl.overlay.settings.misc.MiscSettings

object Settings {
    fun storeAll(
        configPath: String = "./settings/config.properties",
        savedWindowStatePath: String = "./settings/window-state.properties",
        miscPath: String = "./settings/misc.properties"
    ) {
        MiscSettings[FirstTime] = "false"
        StatsToShow.save()
        Config.store(configPath)
        SavedWindowState.store(savedWindowStatePath)
        MiscSettings.store(miscPath)
    }
}