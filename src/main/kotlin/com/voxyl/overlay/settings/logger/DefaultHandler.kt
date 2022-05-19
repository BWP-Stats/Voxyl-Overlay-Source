@file:Suppress("ObjectPropertyName", "MemberVisibilityCanBePrivate", "NonAsciiCharacters")

package com.voxyl.overlay.settings.logger

import java.io.File
import java.util.logging.FileHandler
import java.util.logging.Level
import java.util.logging.SimpleFormatter

object DefaultHandler {
    val path = getPath()

    val `😳` = FileHandler(path).apply {
        level = Level.ALL
        formatter = SimpleFormatter()
    }

    private fun getPath(path: String = System.getenv("APPDATA") + "/.voverlay/logs/voxyl.log"): String {
        val configFile = File(path)
        if (!configFile.exists()) {
            configFile.parentFile.mkdirs()
            configFile.createNewFile()
        }
        return configFile.absolutePath
    }
}