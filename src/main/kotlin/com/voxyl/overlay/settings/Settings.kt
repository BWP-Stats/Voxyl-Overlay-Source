package com.voxyl.overlay.settings

import com.voxyl.overlay.settings.config.Config
import com.voxyl.overlay.settings.window.SavedWindowState
import com.voxyl.overlay.kindasortasomewhatviewmodelsishiguessithinkidkwhatevericantbebotheredsmh.StatsToShow
import com.voxyl.overlay.settings.misc.MiscKeys.FirstTime
import com.voxyl.overlay.settings.misc.MiscSettings
import java.io.File

object Settings {
    fun storeAll(
        basePath: String = "./settings",
        configPath: String = "$basePath/config.properties",
        savedWindowStatePath: String = "$basePath/window-state.properties",
        miscPath: String = "$basePath/misc.properties",
        deleteTempFile: Boolean = true
    ) {
        if (deleteTempFile) deleteTempFile()
        MiscSettings[FirstTime] = "false"
        StatsToShow.save()
        Config.store(configPath)
        SavedWindowState.store(savedWindowStatePath)
        MiscSettings.store(miscPath)
    }

    private fun deleteTempFile() {
        val tempDirPath = File(System.getProperty("java.io.tmpdir"))
        val tempDir = File(tempDirPath, "voverlay")
        tempDir.deleteRecursively()
    }

    fun loadAll(
        basePath: String = "./settings",
        configPath: String = "$basePath/config.properties",
        savedWindowStatePath: String = "$basePath/window-state.properties",
        miscPath: String = "$basePath/misc.properties"
    ) {
        Config.load(configPath)
        SavedWindowState.load(savedWindowStatePath)
        MiscSettings.load(miscPath)
    }
}