package com.voxyl.overlay.ui.mainview.playerstats

import androidx.compose.runtime.toMutableStateList
import com.voxyl.overlay.settings.config.Config
import com.voxyl.overlay.settings.config.ConfigKeys.Columns

object StatsToShow {
    val stats = Config[Columns].split(",").toMutableStateList()
}