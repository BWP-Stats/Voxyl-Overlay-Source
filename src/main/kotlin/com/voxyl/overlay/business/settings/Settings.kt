@file:Suppress("PropertyName")

package com.voxyl.overlay.business.settings

import com.voxyl.overlay.appinfo.AppInfo
import com.voxyl.overlay.business.settings.config.Config
import com.voxyl.overlay.business.settings.misc.CurrentVersion
import com.voxyl.overlay.business.settings.misc.FirstTimeUsingOverlay
import com.voxyl.overlay.business.settings.misc.MiscSettings
import com.voxyl.overlay.business.settings.window.WindowState
import com.voxyl.overlay.controllers.playerstats.StatsToShow
import io.github.aakira.napier.Napier
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*

abstract class Settings<Type : Settings<Type>> {
    abstract val FILE_NAME: String

    private lateinit var state: Properties
    
    operator fun get(key: SettingsKey<Type>): String {
        return state[key.name].toString()
    }

    operator fun set(key: SettingsKey<Type>, value: Any) {
        state[key.name] = value
    }

    fun getOrNullIfBlank(key: SettingsKey<Type>): String? {
        return state.getProperty(key.name)?.takeIf { it.isNotBlank() }
    }

    fun register(key: SettingsKey<Type>) {
        if (key.isInvalidProperty(state)) {
            state.setProperty(key.name, key.default)
        }
    }
    
    fun load(basePath: String = FILE_NAME) {
        state = get(basePath)
    }

    fun get(basePath: String = FILE_NAME): Properties {
        val fullerPath = "$basePath/$FILE_NAME"

        val props = Properties()
        makeConfigFileIfNotPresent(fullerPath)
        props.load(FileInputStream(fullerPath))

        return props
    }

    fun store(basePath: String = FILE_NAME) {
        val fullerPath = "$basePath/$FILE_NAME"

        val configFile = File(fullerPath)

        if (!configFile.exists()) {
            configFile.parentFile.mkdirs()
            configFile.createNewFile()
        }

        state.store(FileOutputStream(fullerPath), null)
    }

    private fun makeConfigFileIfNotPresent(path: String = FILE_NAME) {
        val configFile = File(path)

        if (!configFile.exists()) {
            configFile.parentFile.mkdirs()
            configFile.createNewFile()
        }
    }
    
    companion object {
        const val BASE_PATH = "./settings"

        fun storeAll(basePath: String = BASE_PATH, deleteTempFile: Boolean = false) {
            if (deleteTempFile) deleteTempDir()
            MiscSettings[FirstTimeUsingOverlay] = "false"
            StatsToShow.save()

            Config.store(basePath)
            WindowState.store(basePath)
            MiscSettings.store(basePath)
        }

        fun loadAll(basePath: String = BASE_PATH) {
            Config.load(basePath)
            WindowState.load(basePath)
            MiscSettings.load(basePath)
        }

        fun checkForAutoRestore() {
            if (MiscSettings[CurrentVersion] != AppInfo.VERSION) {
                restoreFromTemp()
            }
        }

        fun backupToTemp() {
            createTempDir()
            storeAll(tempDir.absolutePath)
        }

        fun backupExists(): Boolean {
            val children = tempDir.listFiles() ?: emptyArray()
            
            return tempDir.exists()
                    && children.any { it.name.contains(Config.FILE_NAME) }
                    && children.any { it.name.contains(MiscSettings.FILE_NAME) }
                    && children.any { it.name.contains(WindowState.FILE_NAME) }
        }
        
        fun restoreFromTemp() {
            if (!tempDir.exists()) {
                Napier.i("No previous settings found")
                return
            }

            loadAll(tempDir.absolutePath)
        }

        fun clearBackups() {
            deleteTempDir()
        }

        private val tempDir: File
            get() {
                val tempDirPath = File(System.getProperty("java.io.tmpdir"))
                return File(tempDirPath, "voverlay")
            }

        private fun createTempDir() {
            for (counter in 0 until 1000) {
                if (tempDir.exists() || tempDir.mkdir()) {
                    break
                }
            }
        }

        private fun deleteTempDir() {
            tempDir.deleteRecursively()
        }
    }
}
