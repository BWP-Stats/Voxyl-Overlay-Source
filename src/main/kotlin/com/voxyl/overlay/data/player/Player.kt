package com.voxyl.overlay.data.player

import com.google.gson.JsonObject
import com.voxyl.overlay.data.valueclasses.*
import kotlin.math.min

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
        stats["name"] = playerInfoJson?.json?.get("displayname")?.asString?.trim('"')
            ?: hypixelStatsJson?.json?.getAsJsonObject("player")?.get("displayname")?.asString?.trim('"')
                    ?: name
        stats["uuid"] = uuid
    }

    init {
        playerInfoJson?.let { addStatsFromJsonToStatsMap(it.json, "bwp") }
        overallStatsJson?.let { addStatsFromJsonToStatsMap(it.json, "bwp") }
        gameStatsJson?.let {
            addStatsFromJsonToStatsMap(it.json, "bwp")

            stats += it.toOverallGameStats().map { stat ->
                "bwp.${stat.key}" to stat.value
            }
        }

        stats["bwp.role"] = stats["bwp.role"]?.trim('"') ?: "ERR"

        stats["bwp.realstars"] = calcRealStars(stats["bwp.level"])
    }

    init {
        hypixelStatsJson?.let {
            addStatsFromJsonToStatsMap(
                hypixelStatsJson.json
                    .getAsJsonObject("player")
                    .getAsJsonObject("stats")
                    .getAsJsonObject("Bedwars"),
                prevKey = "bedwars"
            )
        }

        stats["bedwars.level"] = stats["bedwars.experience"]?.toInt()?.toHypixelLevel()?.toInt()?.toString() ?: "ERR"
        stats["bedwars.fkdr"] = calcFkdr(stats)
        stats["bedwars.wlr"] = calcWlr(stats)
    }

    init {
        println(stats)
    }

    private fun addStatsFromJsonToStatsMap(jsonObj: JsonObject, prevKey: String = "") {
        jsonObj.keySet().forEach { key ->
            val value: Any = jsonObj.get(key)

            if (value is JsonObject)
                addStatsFromJsonToStatsMap(value, "$prevKey.$key")
            else stats["$prevKey.$key".lowercase()] = "$value"
        }
    }

    private fun calcRealStars(s: String?): String {
        val level = s?.toIntOrNull() ?: return "ERR"
        val prestige = level / 100
        var bonus = 0
        for (i in 0..prestige) {
            bonus += i * 500
        }
        return ""
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
        val fks = stats["bedwars.final_kills_bedwars"]?.toDouble() ?: return "ERR"
        val fds = stats["bedwars.final_deaths_bedwars"]?.toDouble() ?: return "ERR"

        return if (fds == 0.0) fks.toString() else String.format("%.2f", fks / fds)
    }

    private fun calcWlr(stats: MutableMap<String, String>): String {
        val ws = stats["bedwars.wins_bedwars"]?.toDouble() ?: return "ERR"
        val ls = stats["bedwars.losses_bedwars"]?.toDouble() ?: return "ERR"

        return if (ls == 0.0) ws.toString() else String.format("%.2f", ws / ls)
    }

    operator fun get(key: String): String? = stats[key]
    override fun equals(other: Any?) = name.equals(other as? String, true)
    override fun hashCode() = name.lowercase().hashCode()
}

@Suppress("KotlinConstantConditions")
fun main() {
    println(calcRealStarsTm(210, 2))
}

tailrec fun calcRealStarsTm(level: Int = 1, prestige: Int = level / 100, acc: Int = 0): Int {
    return if (prestige < 0) {
        acc - 1000
    } else {
        var xp: Int
        val relative = level - (prestige * 100)
        val incomplete = min(4 + (prestige / 2), relative)
        xp = (incomplete * (1 + incomplete)) / 2 * 1000
        if (prestige % 2 == 1) xp -= 500
        xp += (relative - incomplete) * (5000 + (prestige * 500))
        calcRealStarsTm(level - relative, prestige - 1, xp + acc)
    }
}
