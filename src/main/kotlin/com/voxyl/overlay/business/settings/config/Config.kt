package com.voxyl.overlay.business.settings.config

import com.voxyl.overlay.business.settings.Settings
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*

object Config : Settings<Config>() {
    override val FILE_NAME = "config.properties"
}
