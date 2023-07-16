package com.voxyl.overlay.controllers.playerstats

import com.google.gson.JsonObject
import com.voxyl.overlay.business.settings.Settings
import com.voxyl.overlay.business.stats.apis.ApiProvider
import com.voxyl.overlay.business.stats.models.LevelsLeaderboardJson
import com.voxyl.overlay.business.stats.models.WWinsLeaderboardJson
import com.voxyl.overlay.business.settings.config.*
import com.voxyl.overlay.business.utils.untrimUUID
import io.github.aakira.napier.Napier
import kotlinx.coroutines.*

object LeaderboardTracker {
    @get:Synchronized @set:Synchronized
    private var _levelLB: LevelsLeaderboardJson? = null

    @get:Synchronized @set:Synchronized
    private var _wwLB: WWinsLeaderboardJson? = null

    val levelLB: JsonObject?
        get() = _levelLB?.json

    val wwLB: JsonObject?
        get() = _wwLB?.json

    @Synchronized
    fun updateLevelLB(cs: CoroutineScope) = cs.launch(Dispatchers.IO) {
        _levelLB = ApiProvider.bwp.getLevelLeaderboard(Config[BwpApiKey]).body()
            ?.let { LevelsLeaderboardJson(it) }
    }

    @Synchronized
    fun updateWWinsLB(cs: CoroutineScope) = cs.launch(Dispatchers.IO) {
        _wwLB = ApiProvider.bwp.getWWLeaderboard(Config[BwpApiKey]).body()
            ?.let { WWinsLeaderboardJson(it) }
    }

    fun findInLevelLB(_uuid: String): JsonObject? {
        val json = levelLB ?: return null
        val uuid = _uuid.untrimUUID()

        val found = json.getAsJsonArray("players").find {
            it.asJsonObject.get("uuid").asString == uuid
        }
        return found?.asJsonObject
    }

    fun findInWWLB(_uuid: String): JsonObject? {
        val json = wwLB ?: return null
        val uuid = _uuid.untrimUUID()

        val found = json.getAsJsonArray("players").find {
            it.asJsonObject.get("uuid").asString == uuid
        }
        return found?.asJsonObject
    }

    fun foundInLevelLB(uuid: String): Boolean {
        return findInLevelLB(uuid) != null
    }

    fun foundInWWLB(uuid: String): Boolean {
        return findInWWLB(uuid) != null
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun startTracking(cs: CoroutineScope = GlobalScope, delay: Long = 300000L) {
        cs.launch {
            while (true) {
                try { updateLevelLB(cs) } catch (e: Exception) { Napier.e("Failed to update level lb", e) }
                try { updateWWinsLB(cs) } catch (e: Exception) { Napier.e("Failed to update wwins lb", e) }
                delay(delay)
            }
        }
    }
}
