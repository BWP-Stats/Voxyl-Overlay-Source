package com.voxyl.overlay.data.player

import com.google.gson.JsonObject
import com.voxyl.overlay.data.dto.BWPStats

class Player(
    val name: String,
    uuid: String,
    bwpStats: BWPStats
) {

    val stats = mutableMapOf<String, String>()

    init {
        with(bwpStats) {
            stats["name"] = playerInfoJson.json["lastLoginName"].toString().trim('"')
            stats["uuid"] = uuid

            addStatsFromJsonToStatsMap(playerInfoJson.json, "bwp")
            addStatsFromJsonToStatsMap(overallStatsJson.json, "bwp")
            addStatsFromJsonToStatsMap(gameStatsJson.json, "bwp")

            stats += gameStatsJson.toOverallGameStats().map {
                "bwp.${it.key}" to it.value
            }

            stats["bwp.role"] = stats["bwp.role"]?.trim('"') ?: "None"
        }
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

    operator fun get(key: String): String? = stats[key]
    override fun equals(other: Any?) = name.equals(other as? String, true)
    override fun hashCode() = name.lowercase().hashCode()
}
