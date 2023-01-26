package com.voxyl.overlay.controllers.playerstats

import androidx.compose.runtime.toMutableStateList
import com.voxyl.overlay.business.settings.config.Columns
import com.voxyl.overlay.business.settings.config.Config
import java.util.*

object StatsToShow {
    val _stats = Config[Columns].split(",").toMutableStateList()

    val stats: List<String>
        get() = _stats.toList()

    fun save() {
        Config[Columns] = _stats.joinToString(",")
    }

    fun add(stat: String) {
        if (!_stats.contains(stat)) {
            _stats.add(stat)
        }
        save()
    }

    fun remove(stat: String) {
        _stats.remove(stat)
        save()
    }
}
