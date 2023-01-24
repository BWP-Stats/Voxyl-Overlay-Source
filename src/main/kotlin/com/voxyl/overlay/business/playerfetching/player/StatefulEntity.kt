package com.voxyl.overlay.business.playerfetching.player

import com.google.gson.JsonObject
import com.voxyl.overlay.business.contains
import com.voxyl.overlay.business.get
import com.voxyl.overlay.business.getStr
import com.voxyl.overlay.business.playerfetching.models.GameStatsJson
import com.voxyl.overlay.business.playerfetching.models.HypixelStatsJson
import com.voxyl.overlay.business.playerfetching.models.OverallStatsJson
import com.voxyl.overlay.business.playerfetching.models.PlayerInfoJson
import com.voxyl.overlay.ui.playerstats.playerstats.colors.ERROR_PLACEHOLDER
import io.github.aakira.napier.Napier
import kotlin.math.ceil
import kotlin.math.min

typealias StatsMap = MutableMap<String, String>

abstract class StatefulEntity(val name: String) {
    protected val stats: StatsMap = mutableMapOf()

    operator fun get(key: String): String? {
        return stats[key]
    }

    override fun equals(other: Any?): Boolean {
        return name.equals(other as? String, true)
    }

    override fun hashCode(): Int {
        return name.lowercase().hashCode()
    }
}

class Bot(name: String) : StatefulEntity(name) {
    init {
        stats["name"] = name

        stats["bwp.role"] = "BOT"
        stats["hypixel.rank"] = stats["bwp.role"]!!

        stats["hypixel.rankColor"] = "dark-gray"
    }
}

class Player(
    name: String,
    uuid: String,
    playerInfoJson: PlayerInfoJson?,
    overallStatsJson: OverallStatsJson?,
    gameStatsJson: GameStatsJson?,
    hypixelStatsJson: HypixelStatsJson?
) : StatefulEntity(name) {
    init {
        stats["uuid"] = uuid

        stats["name"] =
            playerInfoJson.getStr("displayname")
            ?: hypixelStatsJson["player"].getStr("displayname")
            ?: name

        stats["name"] = stats["name"]?.trim('"')!!
    }

    init {
        try {
            playerInfoJson  ?.json.addToStatsMap(prevKey = "bwp")
            overallStatsJson?.json.addToStatsMap(prevKey = "bwp")
            gameStatsJson   ?.json.addToStatsMap(prevKey = "bwp")

            stats += gameStatsJson?.toOverallGameStats()?.map { stat ->
                "bwp.${stat.key}" to stat.value
            } ?: emptyList()

            stats["bwp.role"] = stats["bwp.role"]?.trim('"') ?: ERROR_PLACEHOLDER

            stats["bwp.realstars"] = calcRealStarsTm()
        } catch (e: Exception) {
            Napier.e("Error initializing BWP stats for $name", e)
        }
    }

    init {
        try {
            val playerJson = hypixelStatsJson["player"]

            stats["hypixel.rank"] = getHypixelRank(playerJson)
            stats["hypixel.rankColor"] = playerJson.getStr("rankPlusColor").formatAsMCColor()

            hypixelStatsJson["player", "stats", "Bedwars"]
                .addToStatsMap(prevKey = "bedwars")

            stats["bedwars.level"] = stats["bedwars.experience"]?.toDouble()?.toInt()?.toHypixelLevel()?.toInt()?.toString() ?: "0"
            stats["bedwars.fkdr"] = calcFkdr(stats)
            stats["bedwars.wlr"] = calcWlr(stats)
        } catch (e: Exception) {
            Napier.e("Error initializing Bedwars stats for $name", e)
        }
    }

    private fun getHypixelRank(player: JsonObject?): String {
        return player?.let {
            if (name.lowercase() == "hypixel")
                return@let "OWNER"

            if (name.lowercase() == "technoblade")
                return@let "PIG+++"

            if ("rank" in player) {
                return@let player["rank"].asString
            }

            val isMvpPlusPlus = ("monthlyPackageRank" in player)
                             && ("monthlyRankColor" in player)
                             && player["monthlyPackageRank"].asString != "NONE"

            if (isMvpPlusPlus) {
                return@let if (player["monthlyRankColor"].asString == "AQUA") {
                    "BMVP++"
                } else {
                    "GMVP++"
                }
            }

            if ("newPackageRank" in player) {
                return@let player["newPackageRank"].asString
            }

            null
        }?.replace("_PLUS", "+") ?: "NONE"
    }

    private fun String?.formatAsMCColor(): String {
        return this?.replace("_", "-")?.lowercase() ?: "red"
    }

    private fun JsonObject?.addToStatsMap(prevKey: String = "") {
        this?.keySet()?.forEach { key ->
            val value: Any = this.get(key)

            if (value is JsonObject) {
                value.addToStatsMap("$prevKey.$key")
            } else {
                stats["$prevKey.$key".lowercase()] = "$value"
            }
        }
    }

    private fun calcRealStarsTm(): String {
        if ((stats["bwp.level"]?.toIntOrNull() ?: return "0") <= 100) {
            return stats["bwp.level"]!!
        }

        val exp = calcRawXp(stats["bwp.level"]!!.toInt()) + stats["bwp.exp"]!!.toInt()
        return ceil(exp / 4890.0).toInt().toString()
    }

    private tailrec fun calcRawXp(level: Int = 1, prestige: Int = (level - 1) / 100, acc: Int = 0): Int {
        return if (prestige < 0) {
            acc - 1000
        } else {
            var xp: Int
            val relative = level - (prestige * 100)
            val incomplete = min(4 + (prestige / 2), relative)
            xp = (incomplete * (1 + incomplete)) / 2 * 1000
            if (prestige % 2 == 1) xp -= 500
            xp += (relative - incomplete) * (5000 + (prestige * 500))
            calcRawXp(level - relative, prestige - 1, xp + acc)
        }
    }

    private fun Int.toHypixelLevel(): Double {
        var level = this / 487000 * 100
        var experience = this % 487000
        if (experience < 500) return level + experience / 500.0
        level++
        if (experience < 1500) return level + (experience - 500) / 1000.0
        level++
        if (experience < 3500) return level + (experience - 1500) / 2000.0
        level++
        if (experience < 7000) return level + (experience - 3500) / 3500.0
        level++
        experience -= 7000
        return level + experience / 5000.0
    }
    
    private fun calcFkdr(stats: StatsMap): String {
        val fks = stats["bedwars.final_kills_bedwars"]?.toDouble() ?: return "0"
        val fds = stats["bedwars.final_deaths_bedwars"]?.toDouble() ?: return "0"

        return if (fds == 0.0) fks.toString() else String.format("%.2f", fks / fds)
    }

    private fun calcWlr(stats: StatsMap): String {
        val ws = stats["bedwars.wins_bedwars"]?.toDouble() ?: return "0"
        val ls = stats["bedwars.losses_bedwars"]?.toDouble() ?: return "0"

        return if (ls == 0.0) ws.toString() else String.format("%.2f", ws / ls)
    }
}
