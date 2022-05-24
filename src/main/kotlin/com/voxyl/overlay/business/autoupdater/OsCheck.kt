package com.voxyl.overlay.business.autoupdater

import java.util.Locale

object OsCheck {
    fun getOs(): OsType {
        val os = System.getProperty("os.name", "generic").lowercase(Locale.ENGLISH)

        return when {
            "mac" in os || "darwin" in os -> OsType.MacOS
            "win" in os -> OsType.Windows
            "nux" in os -> OsType.Linux
            else -> OsType.Other
        }
    }
}