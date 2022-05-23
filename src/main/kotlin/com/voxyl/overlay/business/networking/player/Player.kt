package com.voxyl.overlay.business.networking.player

import com.google.gson.JsonObject
import com.voxyl.overlay.business.networking.models.*
import io.github.aakira.napier.Napier
import kotlin.math.ceil
import kotlin.math.min

//TODO: Better initialization; rely less on try catches. I don't have the time to do it right now.
class Player(
    val name: String,
    uuid: String,
    playerInfoJson: PlayerInfoJson?,
    overallStatsJson: OverallStatsJson?,
    gameStatsJson: GameStatsJson?,
    hypixelStatsJson: HypixelStatsJson?
) {

    val stats = mutableMapOf<String, String>()

    init {
        stats["uuid"] = uuid
        try {
            stats["name"] = playerInfoJson?.json?.get("displayname")?.asString?.trim('"')
                ?: hypixelStatsJson?.json?.getAsJsonObject("player")?.get("displayname")?.asString?.trim('"')
                        ?: name
        } catch (e: Exception) {
            stats["name"] = name
            Napier.e("Error initializing name for $name; ${e.message}")
        }
    }

    init {
        try {
            playerInfoJson?.let { addStatsFromJsonToStatsMap(it.json, "bwp") }
            overallStatsJson?.let { addStatsFromJsonToStatsMap(it.json, "bwp") }
            gameStatsJson?.let {
                addStatsFromJsonToStatsMap(it.json, "bwp")

                stats += it.toOverallGameStats().map { stat ->
                    "bwp.${stat.key}" to stat.value
                }
            }

            stats["bwp.role"] = stats["bwp.role"]?.trim('"') ?: "ERR"

            stats["bwp.realstars"] = calcRealStarsTm()
        } catch (e: Exception) {
            Napier.e("Error initializing BWP stats for $name; ${e.message}")
        }
    }

    init {
        try {
            val playerJson = hypixelStatsJson?.json?.getAsJsonObject("player")

            stats["hypixel.rank"] = getRank(playerJson)
            stats["hypixel.rankColor"] = playerJson?.get("rankPlusColor")?.asString.formatAsMCColor()

            addStatsFromJsonToStatsMap(
                hypixelStatsJson?.json
                    ?.getAsJsonObject("player")
                    ?.getAsJsonObject("stats")
                    ?.getAsJsonObject("Bedwars"),
                prevKey = "bedwars"
            )

            stats["bedwars.level"] =
                stats["bedwars.experience"]?.toInt()?.toHypixelLevel()?.toInt()?.toString() ?: "0"
            stats["bedwars.fkdr"] = calcFkdr(stats)
            stats["bedwars.wlr"] = calcWlr(stats)
        } catch (e: Exception) {
            Napier.e("Error initializing Bedwars stats for $name; ${e.message}")
        }
    }

    private fun getRank(playerJson: JsonObject?): String {
        return if (name.lowercase() == "hypixel") {
            "OWNER"
        } else if (name.lowercase() == "technoblade") {
            "PIG+++"
        } else if (playerJson?.get("rank") != null) {
            playerJson.get("rank").asString
        } else if (playerJson?.get("monthlyPackageRank") != null && playerJson.get("monthlyPackageRank").asString != "NONE") {
            if (playerJson.get("monthlyRankColor").asString == "AQUA") {
                "BMVP++"
            } else {
                "GMVP++"
            }
        } else if (playerJson?.get("newPackageRank") != null) {
            playerJson.get("newPackageRank").asString
        } else {
            "NONE"
        }.replace("_PLUS", "+")
    }

    private fun String?.formatAsMCColor(): String {
        return this?.replace("_", "-")?.lowercase() ?: "red"
    }

    private fun addStatsFromJsonToStatsMap(jsonObj: JsonObject?, prevKey: String = "") {
        if (jsonObj == null) return

        jsonObj.keySet().forEach { key ->
            val value: Any = jsonObj.get(key)

            if (value is JsonObject) {
                addStatsFromJsonToStatsMap(value, "$prevKey.$key")
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

    private fun calcFkdr(stats: MutableMap<String, String>): String {
        val fks = stats["bedwars.final_kills_bedwars"]?.toDouble() ?: return "0"
        val fds = stats["bedwars.final_deaths_bedwars"]?.toDouble() ?: return "0"

        return if (fds == 0.0) fks.toString() else String.format("%.2f", fks / fds)
    }

    private fun calcWlr(stats: MutableMap<String, String>): String {
        val ws = stats["bedwars.wins_bedwars"]?.toDouble() ?: return "0"
        val ls = stats["bedwars.losses_bedwars"]?.toDouble() ?: return "0"

        return if (ls == 0.0) ws.toString() else String.format("%.2f", ws / ls)
    }

    operator fun get(key: String): String? = stats[key]
    override fun equals(other: Any?) = name.equals(other as? String, true)
    override fun hashCode() = name.lowercase().hashCode()
}
