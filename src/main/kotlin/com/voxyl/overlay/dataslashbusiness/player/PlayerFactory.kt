package com.voxyl.overlay.dataslashbusiness.player

import com.voxyl.overlay.dataslashbusiness.apis.ApiProvider
import com.voxyl.overlay.dataslashbusiness.apis.BWPApi
import com.voxyl.overlay.dataslashbusiness.apis.HypixelApi
import com.voxyl.overlay.dataslashbusiness.apis.MojangApi
import com.voxyl.overlay.dataslashbusiness.valueclasses.*
import com.voxyl.overlay.dataslashbusiness.homemadesimplecache.HomemadeCache
import com.voxyl.overlay.settings.config.Config
import com.voxyl.overlay.settings.config.ConfigKeys.*
import io.github.aakira.napier.Napier
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

//TODO: Overhaul the API fetching system when there is time; currently it's a mess
object PlayerFactory {
    fun makePlayer(
        name: String,
        bwpApiKey: String = Config[BwpApiKey],
        hypixelApiKey: String = Config[HypixelApiKey],
        bwpApi: BWPApi = ApiProvider.getBWPApi(),
        hypixelApi: HypixelApi = ApiProvider.getHypixelApi(),
    ): Flow<Status<Player>> = flow {

        if (HomemadeCache[name]?.player != null) {
            val player = HomemadeCache[name]?.player ?: makePlayer(name)

            if (player is Player) {
                emit(Status.Loaded(player, name = name))
            }
            return@flow
        }

        try {
            emit(Status.Loading(name = name))

            val uuid = getUUID(name)
            val deferredHypixelStats = getHypixelStats(uuid, hypixelApiKey, hypixelApi)
            val deferredBwpStats = getBWPStats(uuid, bwpApiKey, bwpApi)

            val hypixelStats = try {
                deferredHypixelStats.await()
            } catch (e: Exception) {
                Napier.e("Failed to get Hypixel stats for $name; ${e.localizedMessage}")
                null
            }

            val info = try {
                deferredBwpStats.info.await()
            } catch (e: Exception) {
                Napier.e("Failed to get BWP info for $name; ${e.localizedMessage}")
                null
            }

            val overall = try {
                deferredBwpStats.overall.await()
            } catch (e: Exception) {
                Napier.e("Failed to get BWP overall for $name; ${e.localizedMessage}")
                null
            }

            val game = try {
                deferredBwpStats.game.await()
            } catch (e: Exception) {
                Napier.e("Failed to get BWP game for $name; ${e.localizedMessage}")
                null
            }

            if ((hypixelStats == null || hypixelStats.json["success"].asString == "false")
                && ((info == null || info.json["success"].asString == "false")
                || (overall == null || overall.json["success"].asString == "false")
                || (game == null || game.json["success"].asString == "false"))
            ) throw IOException("Failed to get fetch stats from both APIs for $name")

            emit(Status.Loaded(Player(name, uuid, info, overall, game, hypixelStats), name = name))

        } catch (e: HttpException) {
            Napier.e("HttpException for '$name'; " + e.localizedMessage)
            emit(
                Status.Error(
                    e.localizedMessage ?: "An unexpected error occurred trying to reach an API for '$name'",
                    name = name
                )
            )
        } catch (e: IOException) {
            Napier.e("IOException for '$name'; " + e.localizedMessage)
            emit(
                Status.Error(
                    e.localizedMessage ?: "IOException for '$name'; either your wifi or an API is down",
                    name = name
                )
            )
        }
    }

    private suspend fun getUUID(name: String, mojangApi: MojangApi = ApiProvider.getMojangApi()): String {
        return withContext(Dispatchers.IO) {
            try {
                val uuid = mojangApi.getUUID(name)
                    .substringAfterLast(":")
                    .trim('"', '}')
                    .untrimUUID()

                if (!uuid.matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}".toRegex())) {
                    throw IOException("'$name' doesn't exist or UUID api is down")
                }

                return@withContext uuid
            } catch (e: NullPointerException) {
                throw IOException("UUID null for $name")
            } catch (e: Exception) {
                throw IOException("UUID api is down? " + e.localizedMessage)
            }
        }
    }

    private fun String.untrimUUID(): String {
        return this.replaceRange(8, 8, "-")
            .replaceRange(13, 13, "-")
            .replaceRange(18, 18, "-")
            .replaceRange(23, 23, "-")
    }

    private data class DeferredBWPStats(
        val overall: Deferred<OverallStatsJson>,
        val info: Deferred<PlayerInfoJson>,
        val game: Deferred<GameStatsJson>
    )

    private suspend fun getBWPStats(uuid: String, apiKey: String, bwpApi: BWPApi): DeferredBWPStats {
        return withContext(Dispatchers.IO) {
            DeferredBWPStats(
                async(SupervisorJob()) { OverallStatsJson(bwpApi.getOverallStats(uuid, apiKey)) },
                async(SupervisorJob()) { PlayerInfoJson(bwpApi.getPlayerInfo(uuid, apiKey)) },
                async(SupervisorJob()) { GameStatsJson(bwpApi.getGameStats(uuid, apiKey)) }
            )
        }
    }

    private suspend fun getHypixelStats(
        uuid: String,
        apiKey: String,
        hypixelApi: HypixelApi
    ): Deferred<HypixelStatsJson> {
        return withContext(Dispatchers.IO) {
            async(SupervisorJob()) { HypixelStatsJson(hypixelApi.getStats(uuid, apiKey)) }
        }
    }
}