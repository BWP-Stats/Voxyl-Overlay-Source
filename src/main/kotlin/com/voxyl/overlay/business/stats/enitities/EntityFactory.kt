@file:Suppress("SpellCheckingInspection", "MoveVariableDeclarationIntoWhen")

package com.voxyl.overlay.business.stats.enitities

import com.google.gson.JsonObject
import com.voxyl.overlay.business.settings.config.*
import com.voxyl.overlay.business.stats.apis.ApiProvider
import com.voxyl.overlay.business.stats.apis.MojangApi
import com.voxyl.overlay.business.stats.enitities.types.Bot
import com.voxyl.overlay.business.stats.enitities.types.Player
import com.voxyl.overlay.business.stats.enitities.types.RawEntity
import com.voxyl.overlay.business.stats.models.GameStatsJson
import com.voxyl.overlay.business.stats.models.HypixelStatsJson
import com.voxyl.overlay.business.stats.models.OverallStatsJson
import com.voxyl.overlay.business.stats.models.PlayerInfoJson
import com.voxyl.overlay.business.utils.NetworkingUtils
import com.voxyl.overlay.business.utils.getStrOrNull
import com.voxyl.overlay.controllers.common.Aliases
import io.github.aakira.napier.Napier
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.io.IOException

typealias DR = Deferred<Response<JsonObject>>

object EntityFactory {
    fun create(rawName: String): Flow<ResponseStatus<out RawEntity>> {
        val isBot = rawName.startsWith("Bot-")

        if (Config[AddBotsToOverlay] != "true" && isBot)
            return emptyFlow()

        if (isBot) return makeBot(rawName)

        val name = if (
            Aliases.isStrictlyAlias(rawName) && Config[ShowYourStatsInsteadOfAliases] == "true"
        ) Config[PlayerName] else rawName

        return makePlayer(name)
    }

    private fun makePlayer(name: String) = flow {
        try {
            emit(ResponseStatus.Loading(name = name))

            val uuid = getUUID(name)

            val stats = queryStatsApis(uuid, Config[BwpApiKey], Config[HypixelApiKey])

            val hypixel = validatedHypixelResponse(stats)
            val overall = validatedOverallResponse(stats)
            val game = validatedGameResponse(stats)
            val info = validatedInfoResponse(stats)

            if (hypixel == null && (overall == null || game == null || info == null))
                throw IOException("Failed to get fetch stats from both APIs for $name")

            val player = Player(name, uuid, info, overall, game, hypixel)

            emit(ResponseStatus.Loaded(player, name = name))

        } catch (e: IOException) {
            val msg = e.localizedMessage ?: "IOException for '$name'; either your wifi or an API is down"
            emit(ResponseStatus.Error(msg, name = name))
        }
    }

    private fun makeBot(name: String) = flow<ResponseStatus<out RawEntity>> {
        emit(ResponseStatus.Loading(name = name))
        emit(ResponseStatus.Loaded(Bot(name), name = name))
    }

    private suspend fun getUUID(name: String, mojangApi: MojangApi = ApiProvider.mojang): String {
        val response = mojangApi.getUUID(name)

        if (response.isSuccessful) {
            return response.body()
                ?.getStrOrNull("id")
                ?.validateUUID()
                ?: throw IOException("UUID null or invalid for $name; HTTP ${response.code()}").also { Napier.e(it.localizedMessage) }
        }

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

    private suspend fun validatedHypixelResponse(deferred: Map<String, DR>): HypixelStatsJson? {
        val response = deferred["hypixel"]!!.await()

        if (response.isSuccessful) {
            if (response.body()?.get("player")?.isJsonNull == true) {
                return null
            }

            return response.body()?.let { HypixelStatsJson(it) }
        }
        return null
    }

    private suspend fun validatedOverallResponse(deferred: Map<String, DR>): OverallStatsJson? {
        val response = deferred["overall"]!!.await()

        if (response.isSuccessful) {
            return response.body()?.let { OverallStatsJson(it) }
        }
        return null
    }

    private suspend fun validatedGameResponse(deferred: Map<String, DR>): GameStatsJson? {
        val response = deferred["game"]!!.await()

        if (response.isSuccessful) {
            return response.body()?.let { GameStatsJson(it) }
        }
        return null
    }

    private suspend fun validatedInfoResponse(deferred: Map<String, DR>): PlayerInfoJson? {
        val response = deferred["info"]!!.await()

        if (response.isSuccessful) {
            return response.body()?.let { PlayerInfoJson(it) }
        }
        return null
    }
}
