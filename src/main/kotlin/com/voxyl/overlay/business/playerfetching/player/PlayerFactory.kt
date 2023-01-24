@file:Suppress("SpellCheckingInspection", "MoveVariableDeclarationIntoWhen")

package com.voxyl.overlay.business.playerfetching.player

import com.google.gson.JsonObject
import com.voxyl.overlay.business.NetworkingUtils
import com.voxyl.overlay.business.playerfetching.apis.ApiProvider
import com.voxyl.overlay.business.playerfetching.apis.MojangApi
import com.voxyl.overlay.business.playerfetching.models.GameStatsJson
import com.voxyl.overlay.business.playerfetching.models.HypixelStatsJson
import com.voxyl.overlay.business.playerfetching.models.OverallStatsJson
import com.voxyl.overlay.business.playerfetching.models.PlayerInfoJson
import com.voxyl.overlay.business.settings.config.*
import io.github.aakira.napier.Napier
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import java.io.IOException

typealias DR = Deferred<Response<JsonObject>>

object PlayerFactory {
    fun create(name: String): Flow<ResponseStatus<out StatefulEntity>> {
        val state = preprocessPlayer(name)

        return when (state) {
            is DontMake -> emptyFlow()
            is IsBot -> makeBot(state.name)
            is IsPlayer -> makePlayer(state.name)
        }
    }

    private sealed class State(val name: String)
    private object DontMake               : State("N/A")
    private class  IsBot   (name: String) : State(name)
    private class  IsPlayer(name: String) : State(name)

    private fun preprocessPlayer(rawName: String): State {
        val isBot = rawName.startsWith("Bot-")

        if (Config[AddBotsToOverlay] != "true" && isBot)
            return DontMake

        if (isBot) return IsBot(rawName)

        val name = if (
            Config[Aliases]
                .lowercase()
                .split(",")
                .contains(rawName.lowercase())
            && Config[ShowYourStatsInsteadOfAliases] == "true"
        ) Config[PlayerName] else rawName

        return IsPlayer(name)
    }

    private fun makePlayer(name: String) = flow {
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

            val player = Player(name, uuid, info, overall, game, hypixel)

            emit(ResponseStatus.Loaded(player, name = name))

        } catch (e: IOException) {
            val msg = e.localizedMessage ?: "IOException for '$name'; either your wifi or an API is down"
            emit(ResponseStatus.Error(msg, name = name))
        }
    }

    private fun makeBot(name: String) = flow<ResponseStatus<out StatefulEntity>> {
        emit(ResponseStatus.Loading(name = name))
        emit(ResponseStatus.Loaded(Bot(name), name = name))
    }

    private suspend fun getUUID(name: String, mojangApi: MojangApi = ApiProvider.getMojangApi()): String {
        val response = mojangApi.getUUID(name)

        if (response.isSuccessful) {
            return response.body()
                ?.substringAfterLast(":")
                ?.trim('"', '}')
                ?.validateUUID()
                ?: throw IOException("UUID null or invalid for $name; HTTP ${response.code()}").also { Napier.e(it.localizedMessage) }
        }

        Napier.e("Failed to get UUID for $name; ${NetworkingUtils.formattedError(response)}}")
        throw IOException("Failed to get UUID for $name; ${NetworkingUtils.formattedError(response)}")
    }

    private fun String.validateUUID(): String? {
        return if (this.matches("[0-9a-f]{32}".toRegex())) this else null
    }

    private suspend fun queryStatsApis(uuid: String, bwpKey: String, hypixKey: String): Map<String, DR> {
        return withContext(Dispatchers.IO) {
            mapOf("hypixel" to async { ApiProvider.hypixel.getStats(uuid, hypixKey) },
                  "overall" to async { ApiProvider.bwp.getOverallStats(uuid, bwpKey) },
                  "game" to async { ApiProvider.bwp.getGameStats(uuid, bwpKey) },
                  "info" to async { ApiProvider.bwp.getPlayerInfo(uuid, bwpKey) })
        }
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
