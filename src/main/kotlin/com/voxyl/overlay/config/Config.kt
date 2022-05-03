package com.voxyl.overlay.config

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.Properties

object Config {

    private val config by lazy {
        load()
    }

    private val defaultPath = System.getenv("APPDATA")

    private fun load(path: String = "$defaultPath/.voverlay/config.properties") = Properties().also {
        makeConfigFileIfNotPresent()
        it.load(FileInputStream(path))
        addPropertiesIfNotPresent(it)

        /*it.stringPropertyNames()
            .associateWith { it2 -> it.getProperty(it2) }
            .forEach { it2 -> println(it2) }*/
    }

    private fun addPropertiesIfNotPresent(
        config: Properties = this.config,
    ) {
        ConfigKeys.values().forEach {
            if (propertyIsntValid(it, config)) {
                config.setProperty(it.key, it.defaultValue)
            }
        }
    }

    private fun propertyIsntValid(key: ConfigKeys, config: Properties = this.config) =
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

    fun store(path: String = "$defaultPath/.voverlay/config.properties") {
        config.store(FileOutputStream(path), null)
    }

    operator fun contains(key: String): Boolean {
        return config.getProperty(key) != null && config.getProperty(key) != ""
    }

    private fun makeConfigFileIfNotPresent(path: String = "$defaultPath/.voverlay/config.properties") {
        val configFile = File(path)

        if (!configFile.exists()) {
            configFile.parentFile.mkdirs()
            configFile.createNewFile()
        }
    }
}