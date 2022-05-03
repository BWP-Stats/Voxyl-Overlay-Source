package com.voxyl.overlay.config

import com.voxyl.overlay.WINDOW
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*
import com.voxyl.overlay.config.SavedWindowStateKeys.*

object SavedWindowState {

    private val state by lazy {
        load()
    }

    private val defaultPath = System.getenv("APPDATA")

    private fun load(path: String = "$defaultPath/.voverlay/window-state.properties") = Properties().also {
        makeConfigFileIfNotPresent()
        it.load(FileInputStream(path))
        addPropertiesIfNotPresent(it)

        /*it.stringPropertyNames()
            .associateWith { it2 -> it.getProperty(it2) }
            .forEach { it2 -> println(it2) }*/
    }

    private fun addPropertiesIfNotPresent(
        state: Properties = this.state,
    ) {
        SavedWindowStateKeys.values().forEach {
            if (propertyIsntValid(it, state)) {
                state.setProperty(it.key, it.defaultValue)
            }
        }
    }

    private fun propertyIsntValid(key: SavedWindowStateKeys, state: Properties = this.state) =
        !state.containsKey(key.key) || state[key.key] == null || state.getProperty(key.key).isBlank()

    operator fun get(key: String): String? {
        return state.getProperty(key)
    }

    operator fun get(key: SavedWindowStateKeys): String {
        return state.getProperty(key.key)
    }

    operator fun set(key: String, value: String) {
        state.setProperty(key, value)
    }

    operator fun set(key: SavedWindowStateKeys, value: String) {
        state.setProperty(key.key, value)
    }

    fun getOrNullIfBlank(key: String): String? {
        return state.getProperty(key)?.takeIf { it.isNotBlank() }
    }

    fun getOrNullIfBlank(key: SavedWindowStateKeys): String? {
        return state.getProperty(key.key)?.takeIf { it.isNotBlank() }
    }

    fun store(path: String = "${defaultPath}/.voverlay/window-state.properties") {
        updateSavedWindowState()
        state.store(FileOutputStream(path), null)
    }

    private fun updateSavedWindowState() {
        SavedWindowState[SAVED_X] = WINDOW.x.toString()
        SavedWindowState[SAVED_Y] = WINDOW.y.toString()
        SavedWindowState[SAVED_WIDTH] = WINDOW.width.toString()
        SavedWindowState[SAVED_HEIGHT] = WINDOW.height.toString()
        SavedWindowState[IS_ALWAYS_ON_TOP] = WINDOW.isAlwaysOnTop.toString()
    }

    operator fun contains(key: String): Boolean {
        return state.getProperty(key) != null && state.getProperty(key) != ""
    }

    private fun makeConfigFileIfNotPresent(path: String = "${defaultPath}/.voverlay/window-state.properties") {
        val configFile = File(path)

        if (!configFile.exists()) {
            configFile.parentFile.mkdirs()
            configFile.createNewFile()
        }
    }
}