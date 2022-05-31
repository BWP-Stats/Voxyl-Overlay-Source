package com.voxyl.overlay.business.settings.config

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*

object Config {
    lateinit var config: Properties

    private const val defaultPath = "./settings/config.properties"

    fun load(path: String = defaultPath) {
        config = Properties()
        makeConfigFileIfNotPresent(path)
        config.load(FileInputStream(path))
        addPropertiesIfNotPresent(config)
    }

    private fun addPropertiesIfNotPresent(
        config: Properties = Config.config,
    ) {
        ConfigKeys.values().forEach {
            if (propertyIsNotValid(it, config)) {
                config.setProperty(it.key, it.defaultValue)
            }
        }
    }

    private fun propertyIsNotValid(key: ConfigKeys, config: Properties = Config.config) =
        !config.containsKey(key.key) || config[key.key] == null || config.getProperty(key.key).isBlank()

    operator fun get(key: String): String? {
        return config.getProperty(key)
    }

    operator fun get(key: ConfigKeys): String {
        return config.getProperty(key.key)
    }

    operator fun set(key: String, value: String) {
        config.setProperty(key, value)
    }

    operator fun set(key: ConfigKeys, value: String) {
        config.setProperty(key.key, value)
    }

    fun getOrNullIfBlank(key: String): String? {
        return config.getProperty(key)?.takeIf { it.isNotBlank() }
    }

    fun getOrNullIfBlank(key: ConfigKeys): String? {
        return config.getProperty(key.key)?.takeIf { it.isNotBlank() }
    }

    fun store(path: String = defaultPath) {
        val configFile = File(path)
        if (!configFile.exists()) {
            configFile.parentFile.mkdirs()
            configFile.createNewFile()
        }

        config.store(FileOutputStream(path), null)
    }

    operator fun contains(key: String): Boolean {
        return config.getProperty(key) != null && config.getProperty(key) != ""
    }

    private fun makeConfigFileIfNotPresent(path: String = defaultPath) {
        val configFile = File(path)

        if (!configFile.exists()) {
            configFile.parentFile.mkdirs()
            configFile.createNewFile()
        }
    }
}