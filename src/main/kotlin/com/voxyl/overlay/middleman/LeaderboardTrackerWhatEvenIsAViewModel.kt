package com.voxyl.overlay.middleman

import com.google.gson.JsonObject
import com.voxyl.overlay.data.apis.ApiProvider
import com.voxyl.overlay.data.valueclasses.LevelsLeaderboardJson
import com.voxyl.overlay.data.valueclasses.WWinsLeaderboardJson
import com.voxyl.overlay.settings.config.Config
import com.voxyl.overlay.settings.config.ConfigKeys.BwpApiKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object LeaderboardTrackerWhatEvenIsAViewModel {
    private var _levelLB: LevelsLeaderboardJson? = null
    private var _wwLB: WWinsLeaderboardJson? = null

    var aboutToUpdate = false

    val levelLB: JsonObject?
        get() = if (!aboutToUpdate) _levelLB?.json?.deepCopy() else null

    val wwLB: JsonObject?
        get() = if (!aboutToUpdate) _wwLB?.json?.deepCopy() else null

    fun updateLevelLB(cs: CoroutineScope) = cs.launch(Dispatchers.IO) {
        _levelLB = LevelsLeaderboardJson(
            ApiProvider.getBWPApi().getLevelLeaderboard(Config[BwpApiKey])
        )
    }

    fun updateWWinsLB(cs: CoroutineScope) = cs.launch(Dispatchers.IO) {
        _wwLB = WWinsLeaderboardJson(
            ApiProvider.getBWPApi().getWWLeaderboard(Config[BwpApiKey])
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

    fun startTracking(cs: CoroutineScope, delay: Long = 300000L) {
        cs.launch {
            while (true) {
                updateLevelLB(cs)
                updateWWinsLB(cs)
                delay(delay)
                aboutToUpdate = true
                delay(4L)
                aboutToUpdate = false
            }
        }
    }
}