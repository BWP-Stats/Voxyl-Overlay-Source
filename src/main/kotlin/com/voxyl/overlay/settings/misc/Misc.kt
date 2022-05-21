package com.voxyl.overlay.settings.misc

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*

object Misc {
    private val config by lazy {
        load()
    }

    private val defaultPath = System.getenv("APPDATA")

    private fun load(path: String = "$defaultPath/.voverlay/misc.properties") = Properties().also {
        makeConfigFileIfNotPresent()
        it.load(FileInputStream(path))
        addPropertiesIfNotPresent(it)
    }

    private fun addPropertiesIfNotPresent(
        config: Properties = Misc.config,
    ) {
        MiscKeys.values().forEach {
            if (propertyIsNotValid(it, config)) {
                config.setProperty(it.key, it.defaultValue)
            }
        }
    }

    private fun propertyIsNotValid(key: MiscKeys, config: Properties = Misc.config) =
        !config.containsKey(key.key) || config[key.key] == null || config.getProperty(key.key).isBlank()

    operator fun get(key: String): String? {
        return config.getProperty(key)
    }

    operator fun get(key: MiscKeys): String {
        return config.getProperty(key.key)
    }

    operator fun set(key: String, value: String) {
        config.setProperty(key, value)
    }

    operator fun set(key: MiscKeys, value: String) {
        config.setProperty(key.key, value)
    }

    fun getOrNullIfBlank(key: String): String? {
        return config.getProperty(key)?.takeIf { it.isNotBlank() }
    }

    fun getOrNullIfBlank(key: MiscKeys): String? {
        return config.getProperty(key.key)?.takeIf { it.isNotBlank() }
    }

    fun store(path: String = "$defaultPath/.voverlay/misc.properties") {
        config.store(FileOutputStream(path), null)
    }

    operator fun contains(key: String): Boolean {
        return config.getProperty(key) != null && config.getProperty(key) != ""
    }

    private fun makeConfigFileIfNotPresent(path: String = "$defaultPath/.voverlay/misc.properties") {
        val configFile = File(path)

        if (!configFile.exists()) {
            configFile.parentFile.mkdirs()
            configFile.createNewFile()
        }
    }
}