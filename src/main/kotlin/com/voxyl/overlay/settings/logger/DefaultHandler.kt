@file:Suppress("ObjectPropertyName", "MemberVisibilityCanBePrivate", "NonAsciiCharacters")

package com.voxyl.overlay.settings.logger

import java.io.File
import java.util.logging.FileHandler

object DefaultHandler {
    val path = getPath()

    val `😳` = FileHandler(path)

    private fun getPath(path: String = System.getProperty("user.home") + "/.voxyl/logs/voxyl.log"): String {
        val configFile = File(path)
        if (!configFile.exists()) {
            configFile.parentFile.mkdirs()
            configFile.createNewFile()
        }
        return configFile.absolutePath
    }
}