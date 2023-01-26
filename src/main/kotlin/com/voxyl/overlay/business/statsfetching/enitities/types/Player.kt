package com.voxyl.overlay.business.statsfetching.enitities.types

import com.google.gson.JsonObject
import com.voxyl.overlay.business.statsfetching.models.GameStatsJson
import com.voxyl.overlay.business.statsfetching.models.HypixelStatsJson
import com.voxyl.overlay.business.statsfetching.models.OverallStatsJson
import com.voxyl.overlay.business.statsfetching.models.PlayerInfoJson
import com.voxyl.overlay.business.utils.contains
import com.voxyl.overlay.business.utils.get
import com.voxyl.overlay.business.utils.getStr
import com.voxyl.overlay.ui.entitystats.stats.util.ERROR_PLACEHOLDER
import io.github.aakira.napier.Napier
import kotlin.math.ceil
import kotlin.math.min

class Player(
    name: String,
    uuid: String,
    playerInfoJson: PlayerInfoJson?,
    overallStatsJson: OverallStatsJson?,
    gameStatsJson: GameStatsJson?,
    hypixelStatsJson: HypixelStatsJson?
) : RawEntity(name) {
    init {
        data["uuid"] = uuid

        data["name"] = playerInfoJson.getStr("displayname")
            ?: hypixelStatsJson["player"].getStr("displayname")
            ?: name

        data["name"] = data["name"]?.trim('"')!!
    }

    init {
        try {
            playerInfoJson  ?.json.addToStatsMap(prevKey = "bwp")
            overallStatsJson?.json.addToStatsMap(prevKey = "bwp")
            gameStatsJson   ?.json.addToStatsMap(prevKey = "bwp")

            data += gameStatsJson?.toOverallGameStats()?.map { stat ->
                "bwp.${stat.key}" to stat.value
            } ?: emptyList()

            data["bwp.role"] = data["bwp.role"]?.trim('"') ?: ERROR_PLACEHOLDER

            data["bwp.realstars"] = calcRealStarsTm()
        } catch (e: Exception) {
            Napier.e("Error initializing BWP stats for $name", e)
        }
    }

    init {
        try {
            val playerJson = hypixelStatsJson["player"]

            data["hypixel.rank"] = getHypixelRank(playerJson)
            data["hypixel.rankColor"] = playerJson.getStr("rankPlusColor").formatAsMCColor()

            hypixelStatsJson["player", "stats", "Bedwars"]
                .addToStatsMap(prevKey = "bedwars")

            data["bedwars.level"] = data["bedwars.experience"]?.toDouble()?.toInt()?.toHypixelLevel()?.toInt()?.toString() ?: "0"
            data["bedwars.fkdr"] = calcFkdr(data)
            data["bedwars.wlr"] = calcWlr(data)
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
                data["$prevKey.$key".lowercase()] = "$value"
            }
        }
    }

    private fun calcRealStarsTm(): String {
        if ((data["bwp.level"]?.toIntOrNull() ?: return "0") <= 100) {
            return data["bwp.level"]!!
        }

        val exp = calcRawXp(data["bwp.level"]!!.toInt()) + data["bwp.exp"]!!.toInt()
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

    private fun calcFkdr(stats: Map<String, String>): String {
        val fks = stats["bedwars.final_kills_bedwars"]?.toDouble() ?: return "ERR"
        val fds = stats["bedwars.final_deaths_bedwars"]?.toDouble() ?: return "ERR"

        return if (fds == 0.0) fks.toString() else String.format("%.2f", fks / fds)
    }

    private fun calcWlr(stats: Map<String, String>): String {
        val ws = stats["bedwars.wins_bedwars"]?.toDouble() ?: return "ERR"
        val ls = stats["bedwars.losses_bedwars"]?.toDouble() ?: return "ERR"

        return if (ls == 0.0) ws.toString() else String.format("%.2f", ws / ls)
    }
}
