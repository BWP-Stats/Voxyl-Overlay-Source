package com.voxyl.overlay.business.settings.misc

import com.voxyl.overlay.business.settings.Settings
import com.voxyl.overlay.business.settings.config.Config
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*

object MiscSettings : Settings<MiscSettings>() {
    override val FILE_NAME = "misc.properties"
}
