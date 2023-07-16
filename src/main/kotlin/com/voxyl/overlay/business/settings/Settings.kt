@file:Suppress("PropertyName")

package com.voxyl.overlay.business.settings

import com.voxyl.overlay.business.settings.config.Config
import com.voxyl.overlay.business.settings.misc.FirstTimeUsingOverlay
import com.voxyl.overlay.business.settings.misc.MiscSettings
import com.voxyl.overlay.business.settings.window.WindowState
import com.voxyl.overlay.controllers.playerstats.StatsToShow
import io.github.aakira.napier.Napier
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*

abstract class Settings<Type : Settings<Type>>(val fileName: String) {
    private lateinit var state: Properties

    val keys = mutableListOf<SettingsKey<Type>>()
    
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
        keys += key

        if (key.isInvalidProperty(state)) {
            state.setProperty(key.name, key.default)
        }
    }
    
    fun load(basePath: String = fileName) {
        state = get(basePath)
    }

    fun get(basePath: String = fileName): Properties {
        val fullerPath = "$basePath/$fileName"

        val props = Properties()
        makeConfigFileIfNotPresent(fullerPath)
        props.load(FileInputStream(fullerPath))

        return props
    }

    fun store(basePath: String = fileName) {
        val fullerPath = "$basePath/$fileName"

        val configFile = File(fullerPath)

        if (!configFile.exists()) {
            configFile.parentFile.mkdirs()
            configFile.createNewFile()
        }

        state.store(FileOutputStream(fullerPath), null)
    }

    private fun makeConfigFileIfNotPresent(path: String = fileName) {
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

        fun checkForAutoRestore(): Boolean {
            if (Config.keys.all { Config[it] == it.default }) {
                return restoreFromTemp()
            }
            return false
        }

        fun backupToTemp() {
            createTempDir()
            storeAll(tempDir.absolutePath)
        }

        fun backupExists(): Boolean {
            val children = tempDir.listFiles() ?: emptyArray()
            
            return tempDir.exists()
                    && children.any { it.name.contains(Config.fileName) }
                    && children.any { it.name.contains(MiscSettings.fileName) }
                    && children.any { it.name.contains(WindowState.fileName) }
        }
        
        fun restoreFromTemp(): Boolean {
            if (!tempDir.exists()) {
                Napier.i("No previous settings found")
                return false
            }

            loadAll(tempDir.absolutePath)
            return true
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
