package com.voxyl.overlay.middleman

import com.voxyl.overlay.data.apis.ApiProvider
import com.voxyl.overlay.data.apis.BWPApi
import com.voxyl.overlay.data.apis.HypixelApi
import com.voxyl.overlay.data.apis.MojangApi
import com.voxyl.overlay.data.valueclasses.*
import com.voxyl.overlay.data.homemadesimplecache.HomemadeCache
import com.voxyl.overlay.data.player.Player
import com.voxyl.overlay.data.player.Status
import com.voxyl.overlay.settings.config.Config
import com.voxyl.overlay.settings.config.ConfigKeys.*
import io.github.aakira.napier.Napier
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

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
            val hypixelStats = getHypixelStats(uuid, hypixelApiKey, hypixelApi)
            val bwpStats = getBWPStats(uuid, bwpApiKey, bwpApi)

            emit(Status.Loaded(
                Player(
                    name,
                    uuid,
                    bwpStats.info.await(),
                    bwpStats.overall.await(),
                    bwpStats.game.await(),
                    hypixelStats.await()
                ),
                name = name
            ))

        } catch (e: HttpException) {
            Napier.e(e) { "HttpException for '$name'" }
            emit(Status.Error(
                    e.localizedMessage ?: "An unexpected error occurred trying to reach an API for '$name'",
                    name = name
            ))
        } catch (e: IOException) {
            Napier.e(e) { "IOException for '$name'" }
            emit(Status.Error(
                    e.localizedMessage ?: "IOException for '$name'; either your wifi or an API is down",
                    name = name
            ))
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
            }
        }
    }

    private fun String.untrimUUID(): String {
        return this.replaceRange(8, 8, "-")
            .replaceRange(13, 13, "-")
            .replaceRange(18, 18, "-")
            .replaceRange(23, 23, "-")
    }

    private class BWPStats(
        val overall: Deferred<OverallStatsJson>,
        val info: Deferred<PlayerInfoJson>,
        val game: Deferred<GameStatsJson>
    )

    private suspend fun getBWPStats(uuid: String, apiKey: String, bwpApi: BWPApi): BWPStats {
        return withContext(Dispatchers.IO) {
            BWPStats(
                async { OverallStatsJson(bwpApi.getOverallStats(uuid, apiKey)) },
                async { PlayerInfoJson(bwpApi.getPlayerInfo(uuid, apiKey)) },
                async { GameStatsJson(bwpApi.getGameStats(uuid, apiKey)) }
            )
        }
    }

    private suspend fun getHypixelStats(
        uuid: String,
        apiKey: String,
        hypixelApi: HypixelApi
    ): Deferred<HypixelStatsJson> {
        return withContext(Dispatchers.IO) {
            async { HypixelStatsJson(hypixelApi.getStats(uuid, apiKey)) }
        }
    }
}