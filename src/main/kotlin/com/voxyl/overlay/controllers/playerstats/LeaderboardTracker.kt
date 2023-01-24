package com.voxyl.overlay.controllers.playerstats

import com.google.gson.JsonObject
import com.voxyl.overlay.business.statsfetching.apis.ApiProvider
import com.voxyl.overlay.business.statsfetching.models.LevelsLeaderboardJson
import com.voxyl.overlay.business.statsfetching.models.WWinsLeaderboardJson
import com.voxyl.overlay.business.settings.config.*
import io.github.aakira.napier.Napier
import kotlinx.coroutines.*

object LeaderboardTracker {
    private var _levelLB: LevelsLeaderboardJson? = null
    private var _wwLB: WWinsLeaderboardJson? = null

    var aboutToUpdate = false

    val levelLB: JsonObject?
        get() = if (!aboutToUpdate) _levelLB?.json?.deepCopy() else null

    val wwLB: JsonObject?
        get() = if (!aboutToUpdate) _wwLB?.json?.deepCopy() else null

    fun updateLevelLB(cs: CoroutineScope) = cs.launch(Dispatchers.IO) {
        _levelLB = LevelsLeaderboardJson(
            ApiProvider.getActualBwpApi().getLevelLeaderboard(Config[BwpApiKey]).body()!!
        )
    }

    fun updateWWinsLB(cs: CoroutineScope) = cs.launch(Dispatchers.IO) {
        _wwLB = WWinsLeaderboardJson(
            ApiProvider.getActualBwpApi().getWWLeaderboard(Config[BwpApiKey]).body()!!
        )
    }

    fun findInLevelLB(uuid: String): JsonObject? {
        val json = levelLB ?: return null
        val found = json.getAsJsonArray("players").find {
            it.asJsonObject.get("uuid").asString == uuid
        }
        return found?.asJsonObject
    }

    fun findInWWLB(uuid: String): JsonObject? {
        val json = wwLB ?: return null
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
                aboutToUpdate = true
                try { updateLevelLB(cs) } catch (e: Exception) { Napier.e("Failed to update level lb", e) }
                try { updateWWinsLB(cs) } catch (e: Exception) { Napier.e("Failed to update w-wins lb", e) }
                aboutToUpdate = false
                delay(delay)
            }
        }
    }
}
