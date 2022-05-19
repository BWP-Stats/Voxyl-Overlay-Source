package com.voxyl.overlay.ui.mainview.playerstats

import androidx.compose.runtime.toMutableStateList
import com.voxyl.overlay.settings.config.Config
import com.voxyl.overlay.settings.config.ConfigKeys.Columns
import java.util.*

object StatsToShow {
    val stats = Config[Columns].split(",").toMutableStateList()

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

    val addableStats get() = availableStats.filter { it.raw !in stats }

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
        Config[Columns] = stats.joinToString(",")
    }

    fun add(stat: String) {
        if (!stats.contains(stat)) {
            stats.add(stat)
        }
        save()
    }

    fun remove(stat: String) {
        stats.remove(stat)
        save()
    }

    operator fun contains(stat: String) = stats.contains(stat)
}