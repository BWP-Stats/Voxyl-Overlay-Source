package com.voxyl.overlay.settings.misc

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*

object MiscSettings {
    lateinit var misc: Properties

    private const val defaultPath = "./settings/misc.properties"

    fun load(path: String = defaultPath) {
        misc = Properties()
        makeConfigFileIfNotPresent(path)
        misc.load(FileInputStream(path))
        addPropertiesIfNotPresent(misc)
    }

    private fun addPropertiesIfNotPresent(
        misc: Properties = MiscSettings.misc,
    ) {
        MiscKeys.values().forEach {
            if (propertyIsNotValid(it, misc)) {
                misc.setProperty(it.key, it.defaultValue)
            }
        }
    }

    private fun propertyIsNotValid(key: MiscKeys, misc: Properties = MiscSettings.misc) =
        !misc.containsKey(key.key) || misc[key.key] == null || misc.getProperty(key.key).isBlank()

    operator fun get(key: String): String? {
        return misc.getProperty(key)
    }

    operator fun get(key: MiscKeys): String {
        return misc.getProperty(key.key)
    }

    operator fun set(key: String, value: String) {
        misc.setProperty(key, value)
    }

    operator fun set(key: MiscKeys, value: String) {
        misc.setProperty(key.key, value)
    }

    fun getOrNullIfBlank(key: String): String? {
        return misc.getProperty(key)?.takeIf { it.isNotBlank() }
    }

    fun getOrNullIfBlank(key: MiscKeys): String? {
        return misc.getProperty(key.key)?.takeIf { it.isNotBlank() }
    }

    fun store(path: String = defaultPath) {
        misc.store(FileOutputStream(path), null)
    }

    operator fun contains(key: String): Boolean {
        return misc.getProperty(key) != null && misc.getProperty(key) != ""
    }

    private fun makeConfigFileIfNotPresent(path: String = defaultPath) {
        val configFile = File(path)

        if (!configFile.exists()) {
            configFile.parentFile.mkdirs()
            configFile.createNewFile()
        }
    }
}