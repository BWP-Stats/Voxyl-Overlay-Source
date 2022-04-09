package com.voxyl.overlay.data.player

import com.google.gson.JsonObject
import com.voxyl.overlay.config.Config
import com.voxyl.overlay.config.Config.Keys.*
import com.voxyl.overlay.data.apis.ApiProvider
import com.voxyl.overlay.data.apis.BWPApi
import com.voxyl.overlay.data.apis.UUIDApi
import com.voxyl.overlay.data.dto.BWPStats
import com.voxyl.overlay.data.dto.GameStatsJson
import com.voxyl.overlay.data.dto.OverallStatsJson
import com.voxyl.overlay.data.dto.PlayerInfoJson
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class Player private constructor(
    val name: String,
    val uuid: String,
    val bwpStats: BWPStats
) {

    val stats = mutableMapOf<String, String>()

    init {
        with(bwpStats) {
            stats["name"] = name
            stats["uuid"] = uuid
            addStatsFromJsonToStatsMap(playerInfoJson.json, "bwp")
            addStatsFromJsonToStatsMap(overallStatsJson.json, "bwp")
            addStatsFromJsonToStatsMap(gameStatsJson.json, "bwp")
            stats +=  gameStatsJson.toOverallGameStats().map {
                "bwp.${it.key}" to it.value
            }
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

    override fun equals(other: Any?) = name == other
    override fun hashCode() = name.hashCode()

    companion object {
        fun makePlayer(
            name: String,
            apiKey: String = Config[BwpApiKey.key] ?: "",
            bwpApi: BWPApi = ApiProvider.getBWPApi()
        ): Flow<Status<Player>> = flow {

            try {
                emit(Status.Loading(playerName = name))

                lateinit var uuid: String
                runBlocking {
                    uuid = getUUID(name)
                }

                emit(
                    Status.Loaded(
                        Player(name, uuid, getBWPStats(uuid = uuid, apiKey, bwpApi)), name
                    )
                )
            } catch (e: HttpException) {
                emit(
                    Status.Error(
                        e.localizedMessage ?: "An unexpected error occurred trying to reach the BWP API for '$name'",
                        playerName = name
                    )
                )
                println(e.localizedMessage)
            } catch (e: IOException) {
                println("IOException: ${e.localizedMessage}")
                emit(
                    Status.Error(
                        e.localizedMessage ?: "IOException for '$name'; either your wifi or the BWP API is down",
                        playerName = name
                    )
                )
            }
        }

        private suspend fun getUUID(name: String, uuidApi: UUIDApi = ApiProvider.getUUIDApi()): String {
            val uuid = uuidApi.getUUID(name)

            if (!uuid.matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}".toRegex())) {
                throw IOException("'$name' doesn't exist or UUID api is down")
            }

            return uuid
        }

        private suspend fun getBWPStats(uuid: String, apiKey: String, bwpApi: BWPApi): BWPStats {
            lateinit var playerInfoJson: JsonObject
            lateinit var overallStatsJson: JsonObject
            lateinit var gameStatsJson: JsonObject

            println("1")

            runBlocking {
                launch(Dispatchers.IO) { playerInfoJson = bwpApi.getPlayerInfo(uuid, apiKey) }
                launch(Dispatchers.IO) { overallStatsJson = bwpApi.getOverallStats(uuid, apiKey) }
                launch(Dispatchers.IO) { gameStatsJson = bwpApi.getGameStats(uuid, apiKey) }
            }

            println("2")

            return BWPStats(
                OverallStatsJson(overallStatsJson),
                PlayerInfoJson(playerInfoJson),
                GameStatsJson(gameStatsJson)
            )
        }
    }
}
