@file:Suppress("SpellCheckingInspection")

package com.voxyl.overlay.business.playerfetching.player

import com.google.gson.JsonObject
import com.voxyl.overlay.business.NetworkingUtils
import com.voxyl.overlay.business.playerfetching.apis.ApiProvider
import com.voxyl.overlay.business.playerfetching.apis.BwpApi
import com.voxyl.overlay.business.playerfetching.apis.MojangApi
import com.voxyl.overlay.business.playerfetching.models.GameStatsJson
import com.voxyl.overlay.business.playerfetching.models.HypixelStatsJson
import com.voxyl.overlay.business.playerfetching.models.OverallStatsJson
import com.voxyl.overlay.business.playerfetching.models.PlayerInfoJson
import com.voxyl.overlay.business.settings.config.Config
import com.voxyl.overlay.business.settings.config.ConfigKeys.BwpApiKey
import com.voxyl.overlay.business.settings.config.ConfigKeys.HypixelApiKey
import io.github.aakira.napier.Napier
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import java.io.IOException

typealias DR = Deferred<Response<JsonObject>>

object PlayerFactory {
    fun makePlayer(name: String): Flow<ResponseStatus<Player>> = flow {
        try {
            emit(ResponseStatus.Loading(name = name))

            val uuid = getUUID(name)

            val stats = queryStatsApis(uuid, Config[BwpApiKey], Config[HypixelApiKey])

            val hypixel = validatedHypixelResponse(stats, name)
            val overall = validatedOverallResponse(stats, name)
            val game = validatedGameResponse(stats, name)
            val info = validatedInfoResponse(stats, name)

            if (hypixel == null && (overall == null || game == null || info == null))
                throw IOException("Failed to get fetch stats from both APIs for $name")

            emit(ResponseStatus.Loaded(Player(name, uuid, info, overall, game, hypixel), name = name))

        } catch (e: IOException) {
            val msg = e.localizedMessage ?: "IOException for '$name'; either your wifi or an API is down"
            emit(ResponseStatus.Error(msg, name = name))
        }
    }

    private suspend fun getUUID(name: String, mojangApi: MojangApi = ApiProvider.getMojangApi()): String {
        val response = mojangApi.getUUID(name)

        if (response.isSuccessful) {
            return response.body()
                ?.substringAfterLast(":")
                ?.trim('"', '}')
                ?.untrimUUID()
                ?.validateUUID()
                ?: throw IOException("UUID null or invalid for $name; HTTP ${response.code()}").also { Napier.e(it.localizedMessage) }
        }

        Napier.e("Failed to get UUID for $name; ${NetworkingUtils.formattedError(response)}}")
        throw IOException("Failed to get UUID for $name; ${NetworkingUtils.formattedError(response)}")
    }

    private fun String.untrimUUID(): String {
        return this.replaceRange(8, 8, "-")
            .replaceRange(13, 13, "-")
            .replaceRange(18, 18, "-")
            .replaceRange(23, 23, "-")
    }

    private fun String.validateUUID(): String? {
        return if (this.matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}".toRegex())) this else null
    }

    private suspend fun queryStatsApis(uuid: String, bwpKey: String, hypixKey: String): Map<String, DR> {
        return withContext(Dispatchers.IO) {
            mapOf("hypixel" to async { ApiProvider.getHypixelApi().getStats(uuid, hypixKey) },
                "overall" to async { getBwpApi().getOverallStats(uuid, bwpKey) },
                "game" to async { getBwpApi().getGameStats(uuid, bwpKey) },
                "info" to async { getBwpApi().getPlayerInfo(uuid, bwpKey) })
        }
    }

    private fun getBwpApi(): BwpApi {
        return if (Config["use_backup_bwp_api"] != "false") ApiProvider.getBackupBwpApi() else ApiProvider.getActualBwpApi()
    }

    private suspend fun validatedHypixelResponse(deferred: Map<String, DR>, name: String): HypixelStatsJson? {
        val response = deferred["hypixel"]!!.await()

        if (response.isSuccessful) {
            if (response.body()?.get("player")?.isJsonNull == true) {
                Napier.e("Failed to get Hypixel stats for $name; Player was null")
                return null
            }

            return response.body()?.let { HypixelStatsJson(it) }
        }

        Napier.e("Failed to get Hypixel stats for $name; ${NetworkingUtils.formattedError(response)}")
        return null
    }

    private suspend fun validatedOverallResponse(deferred: Map<String, DR>, name: String): OverallStatsJson? {
        val response = deferred["overall"]!!.await()

        if (response.isSuccessful) {
            return response.body()?.let { OverallStatsJson(it) }
        }

        Napier.e("Failed to get BWP overall stats for $name; ${NetworkingUtils.formattedError(response)}")
        return null
    }

    private suspend fun validatedGameResponse(deferred: Map<String, DR>, name: String): GameStatsJson? {
        val response = deferred["game"]!!.await()

        if (response.isSuccessful) {
            return response.body()?.let { GameStatsJson(it) }
        }

        Napier.e("Failed to get BWP game stats for $name; ${NetworkingUtils.formattedError(response)}")
        return null
    }

    private suspend fun validatedInfoResponse(deferred: Map<String, DR>, name: String): PlayerInfoJson? {
        val response = deferred["info"]!!.await()

        if (response.isSuccessful) {
            return response.body()?.let { PlayerInfoJson(it) }
        }

        Napier.e("Failed to get BWP player info for $name; ${NetworkingUtils.formattedError(response)}")
        return null
    }
}
