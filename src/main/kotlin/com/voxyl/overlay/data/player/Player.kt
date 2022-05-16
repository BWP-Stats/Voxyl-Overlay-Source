package com.voxyl.overlay.data.player

import com.google.gson.JsonObject
import com.voxyl.overlay.settings.config.Config
import com.voxyl.overlay.settings.config.ConfigKeys.BwpApiKey
import com.voxyl.overlay.data.apis.ApiProvider
import com.voxyl.overlay.data.apis.BWPApi
import com.voxyl.overlay.data.apis.UUIDApi
import com.voxyl.overlay.data.dto.BWPStats
import com.voxyl.overlay.data.dto.GameStatsJson
import com.voxyl.overlay.data.dto.OverallStatsJson
import com.voxyl.overlay.data.dto.PlayerInfoJson
import com.voxyl.overlay.data.homemadesimplecache.HomemadeCache
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

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

    override fun equals(other: Any?) = name == other
    override fun hashCode() = name.hashCode()
}
