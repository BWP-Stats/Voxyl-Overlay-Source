package com.voxyl.overlay.kindasortasomewhatviewmodelsishiguessithinkidkwhatevericantbebotheredsmh

import com.google.gson.JsonObject
import com.voxyl.overlay.business.networking.apis.ApiProvider
import com.voxyl.overlay.business.networking.valueclasses.LevelsLeaderboardJson
import com.voxyl.overlay.business.networking.valueclasses.WWinsLeaderboardJson
import com.voxyl.overlay.settings.config.Config
import com.voxyl.overlay.settings.config.ConfigKeys.BwpApiKey
import io.github.aakira.napier.Napier
import kotlinx.coroutines.*

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

    @OptIn(DelicateCoroutinesApi::class)
    fun startTracking(cs: CoroutineScope = GlobalScope, delay: Long = 300000L) {
        cs.launch {
            while (true) {
                aboutToUpdate = true
                try { updateLevelLB(cs) } catch (e: Exception) { Napier.e("Failed to update level lb") }
                try { updateWWinsLB(cs) } catch (e: Exception) { Napier.e("Failed to update w-wins lb") }
                aboutToUpdate = false
                delay(delay)
            }
        }
    }
}