package com.voxyl.overlay.kindasortasomewhatviewmodelsishiguessithinkidkwhatevericantbebotheredsmh

import androidx.compose.runtime.toMutableStateList
import com.voxyl.overlay.settings.config.Config
import com.voxyl.overlay.settings.config.ConfigKeys.Columns
import java.util.*

object StatsToShow {
    val _stats = Config[Columns].split(",").toMutableStateList()

    val stats: List<String>
        get() = _stats.toList()

    val availableStats = listOf(
        "Tags" `2` "tags",
        "Name" `2` "name",
        "Levelᴮ" `2` "bwp.level",
        "W-Winsᴮ" `2` "bwp.weightedwins",
        "Winsᴮ" `2` "bwp.wins",
        "Bedsᴮ" `2` "bwp.beds",
        "Killsᴮ" `2` "bwp.kills",
        "Finalsᴮ" `2` "bwp.finals",
        "RealStars™" `2` "bwp.realstars",
        "Levelᴴ" `2` "bedwars.level",
        "Fkdrᴴ" `2` "bedwars.fkdr",
        "Wlrᴴ" `2` "bedwars.wlr",
        "Finalsᴴ" `2` "bedwars.final_kills_bedwars",
        "Winsᴴ" `2` "bedwars.wins_bedwars",
    )

    val addableStats get() = availableStats.filter { it.raw !in _stats }

    fun String.raw() = availableStats.find { it.clean == this }?.raw ?: this
    fun String.clean() = availableStats.find { it.raw == this }?.clean ?: this.replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(
            Locale.getDefault()
        ) else it.toString()
    }

    @JvmInline
    value class Stat(private val stat: Pair<String, String>) {
        val clean get() = stat.first
        val raw get() = stat.second
    }

    infix fun String.`2`(stat: String) = Stat(this to stat)

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

    operator fun contains(stat: String) = _stats.contains(stat)
}