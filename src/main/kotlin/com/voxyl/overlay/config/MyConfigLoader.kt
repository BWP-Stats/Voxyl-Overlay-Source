package com.voxyl.overlay.config

import java.io.File
import java.io.IOException
import java.util.Properties

fun main() {
    Config.config.stringPropertyNames()
        .associateWith { Config.config.getProperty(it) }
        .forEach { println(it) }
}

object Config {
    val config by lazy {
        load()
    }

    enum class Keys(var key: String, var defaultValue: String) {
        HypixelApiKey("hypixel_api_key", ""),
        BwpApiKey("bwp_api_key", ""),
        LogFilePath("log_file_path", ""),
        PlayerName("player_name", ""),

        PinYourselfToTop("pin_yourself_to_top", "false"),
        AddYourselfToOverlay("add_yourself_to_overlay", "true"),
        AutoShowAndHide("auto_show_and_hide", "false"),
        AutoShowAndHideDelay("auto_show_and_hide_delay", "5"),
    }

    private val home = System.getProperty("user.home");

    private fun load(path: String = "$home/.voverlay/config.properties") = Properties().also {
        makeConfigFileIfNotPresent()
        it.load(File(path).inputStream())
        addPropertiesIfNotPresent(it)
    }

    private fun addPropertiesIfNotPresent(
        config: Properties,
        path: String = "$home/.voverlay/config.properties"
    ) {
        Keys.values().forEach {
            with(it) {
                if (!config.containsKey(key) || config[key] == null || config.getProperty(key).isBlank()) {
                    config.setProperty(key, defaultValue)
                }
            }
        }
        config.store(File(path).outputStream(), null)
    }

    private fun changeProperty(
        key: String,
        newValue: String,
        path: String = "$home/.voverlay/config.properties"
    ): String {
        val oldValue = config.getProperty(key)

        config.setProperty(key, newValue)
        config.store(File(path).outputStream(), null)

        return oldValue
    }

    operator fun get(key: String): String? {
        return config.getProperty(key)
    }

    fun getOrNullIfBlank(key: String): String? {
        return config.getProperty(key).ifBlank { null }
    }

    operator fun set(key: String, value: String) {
        config.setProperty(key, value)
        config.store(File("$home/.voverlay/config.properties").outputStream(), null)
    }

    fun set(key: String, value: String, path: String = "$home/.voverlay/config.properties") {
        config.setProperty(key, value)
        config.store(File(path).outputStream(), null)
    }

    operator fun contains(key: String): Boolean {
        return config.getProperty(key) != null && config.getProperty(key) != ""
    }

    private fun makeConfigFileIfNotPresent(path: String = "$home/.voverlay/config.properties") {
        makeDirectoryIfNotPresent()

        val userProperties = File(path)

        if (!userProperties.exists()) {
            try {
                userProperties.createNewFile()
            } catch (_: IOException) {
            }
        }
    }

    private fun makeDirectoryIfNotPresent(path: String = "$home/.voverlay") {
        val directory = File(path)

        if (!directory.exists()) {
            try {
                directory.mkdir()
            } catch (_: IOException) {
            }
        }
    }
}