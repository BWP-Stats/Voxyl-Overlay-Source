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

class Player private constructor(
    val name: String,
    val uuid: String,
    val bwpStats: BWPStats
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

    companion object {
        fun makePlayer(
            name: String,
            apiKey: String = Config[BwpApiKey],
            bwpApi: BWPApi = ApiProvider.getBWPApi()
        ): Flow<Status<Player>> = flow {

            println("Making player $name")


            if (HomemadeCache[name]?.player != null) {
                val player = HomemadeCache[name]?.player ?: makePlayer(name)

                println("Found player $name in cache maybe")

                if (player is Player) {
                    println("Player $name found in cache")
                    emit(Status.Loaded(player, name = name))
                }
                return@flow
            }

            try {
                emit(Status.Loading(name = name))

                lateinit var uuid: String
                runBlocking {
                    uuid = getUUID(name)
                }

                emit(
                    Status.Loaded(
                        Player(name, uuid, getBWPStats(uuid, apiKey, bwpApi)),
                        name = name
                    )
                )
            } catch (e: HttpException) {
                emit(
                    Status.Error(
                        e.localizedMessage ?: "An unexpected error occurred trying to reach the BWP API for '$name'",
                        name = name
                    )
                )
                println(e.localizedMessage)
            } catch (e: IOException) {
                println("IOException: ${e.localizedMessage}")
                emit(
                    Status.Error(
                        e.localizedMessage ?: "IOException for '$name'; either your wifi or the BWP API is down",
                        name = name
                    )
                )
            }
        }

        private suspend fun getUUID(name: String, uuidApi: UUIDApi = ApiProvider.getUUIDApi()): String {
            return withContext(Dispatchers.IO) {
                val uuid = uuidApi.getUUID(name)

                if (!uuid.matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}".toRegex())) {
                    throw IOException("'$name' doesn't exist or UUID api is down")
                }

                return@withContext uuid
            }
        }

        private suspend fun getBWPStats(uuid: String, apiKey: String, bwpApi: BWPApi): BWPStats {
            return withContext(Dispatchers.IO) {
                val playerInfoJson = async { bwpApi.getPlayerInfo(uuid, apiKey) }
                val overallStatsJson = async { bwpApi.getOverallStats(uuid, apiKey) }
                val gameStatsJson = async { bwpApi.getGameStats(uuid, apiKey) }

                return@withContext BWPStats(
                    OverallStatsJson(overallStatsJson.await()),
                    PlayerInfoJson(playerInfoJson.await()),
                    GameStatsJson(gameStatsJson.await())
                )
            }
        }
    }
}
