package com.voxyl.overlay.middleman

import com.voxyl.overlay.data.apis.ApiProvider
import com.voxyl.overlay.data.apis.BWPApi
import com.voxyl.overlay.data.apis.UUIDApi
import com.voxyl.overlay.data.dto.BWPStats
import com.voxyl.overlay.data.dto.GameStatsJson
import com.voxyl.overlay.data.dto.OverallStatsJson
import com.voxyl.overlay.data.dto.PlayerInfoJson
import com.voxyl.overlay.data.homemadesimplecache.HomemadeCache
import com.voxyl.overlay.data.player.Player
import com.voxyl.overlay.data.player.Status
import com.voxyl.overlay.settings.config.Config
import com.voxyl.overlay.settings.config.ConfigKeys
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

object PlayerFactory {
    fun makePlayer(
        name: String,
        tryAgainOnTimeout: Boolean = false,
        apiKey: String = Config[ConfigKeys.BwpApiKey],
        bwpApi: BWPApi = ApiProvider.getBWPApi()
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

            emit(
                Status.Loaded(
                    Player(name, uuid, getBWPStats(uuid, apiKey, bwpApi)),
                    name = name
                )
            )

        } catch (e: HttpException) {
            emit(
                Status.Error(
                    e.localizedMessage ?: "An unexpected error occurred trying to reach an API for '$name'",
                    name = name
                )
            )
            println(e.localizedMessage)
        } catch (e: IOException) {
            println("IOException: ${e.localizedMessage}")
            emit(
                Status.Error(
                    e.localizedMessage ?: "IOException for '$name'; either your wifi or an API is down",
                    name = name
                )
            )
        } catch (e: TimeoutCancellationException) {
            println("TimeoutCancellationException: ${e.localizedMessage}")
            if (tryAgainOnTimeout) {
                PlayerKindaButNotExactlyViewModel.remove(name)
                makePlayer(name, false, apiKey, bwpApi)
            }
        }
    }

    private suspend fun getUUID(name: String, uuidApi: UUIDApi = ApiProvider.getUUIDApi()): String {
        return withContext(Dispatchers.IO) {
            try {
                val uuid = uuidApi.getUUID(name)
                    .substringAfterLast(":")
                    .trim('"', '}')
                    .untrimUUID()

                if (!uuid.matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}".toRegex())) {
                    throw IOException("'$name' doesn't exist or UUID api is down")
                }

                return@withContext uuid
            } catch (e: NullPointerException) {
                throw IOException("UUID null for $name")
            }
        }
    }

    private fun String.untrimUUID(): String {
        return this.replaceRange(8, 8, "-")
            .replaceRange(13, 13, "-")
            .replaceRange(18, 18, "-")
            .replaceRange(23, 23, "-")
    }

    private suspend fun getBWPStats(uuid: String, apiKey: String, bwpApi: BWPApi): BWPStats {
        return withContext(Dispatchers.IO) {
            val playerInfoJson = async { bwpApi.getPlayerInfo(uuid, apiKey) }
            val overallStatsJson = async { bwpApi.getOverallStats(uuid, apiKey) }
            val gameStatsJson = async { bwpApi.getGameStats(uuid, apiKey) }

            println("Getting player info for $uuid")

            return@withContext BWPStats(
                OverallStatsJson(overallStatsJson.await()),
                PlayerInfoJson(playerInfoJson.await()),
                GameStatsJson(gameStatsJson.await())
            )
        }
    }
}