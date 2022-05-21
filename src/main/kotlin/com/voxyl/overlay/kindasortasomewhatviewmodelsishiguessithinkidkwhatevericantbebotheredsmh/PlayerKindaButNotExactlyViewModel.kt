package com.voxyl.overlay.kindasortasomewhatviewmodelsishiguessithinkidkwhatevericantbebotheredsmh

import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.*
import com.voxyl.overlay.dataslashbusiness.homemadesimplecache.HomemadeCache
import com.voxyl.overlay.dataslashbusiness.player.*
import com.voxyl.overlay.settings.config.Config
import com.voxyl.overlay.settings.config.ConfigKeys.PlayerName
import io.github.aakira.napier.Napier
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

object PlayerKindaButNotExactlyViewModel {

    private var _players = mutableStateListOf<PlayerState>()

    val players: List<PlayerState>
        get() = _players

    private var jobs = mutableMapOf<String, Job>()

    fun add(name: String, cs: CoroutineScope, vararg tags: Tags) {
        try {
            val tags2 = (listOf(*tags) + generatePreTags(name)).toTypedArray()

            jobs[name] = PlayerFactory.makePlayer(name).onEach {
                _players += when (it) {
                    is Status.Loaded -> {
                        _players.remove(name)
                        PlayerState(
                            name = (it.data as Player)["name"] ?: name,
                            player = it.data,
                            tags = mutableStateListOf(*tags2)
                        ).also { ps ->
                            HomemadeCache.add(ps)
                            try {
                                ps.tags += generatePostTags(ps)
                            } catch (e: Exception) {
                                Napier.wtf(e) { "Failed to generate post tags" }
                            }
                        }
                    }
                    is Status.Loading -> {
                        _players.remove(name)
                        PlayerState(name = name, isLoading = true, tags = mutableStateListOf(*tags2))
                    }
                    is Status.Error -> {
                        _players.remove(name)
                        PlayerState(
                            name = name,
                            error = it.message ?: "An unexpected error has occurred",
                            tags = mutableStateListOf(*tags2 + Tags.Error)
                        ).also { ps ->
                            HomemadeCache.add(ps)
                        }
                    }
                }
            }.launchIn(cs)
        } catch (e: Exception) {
            jobs[name]?.cancel()
            _players.remove(name)
            Napier.wtf(e) { "Failed to add player" }
        }
    }

    private val devNames = listOf("ambmt", "_lightninq", "vitroid", "firestarad", "sirjosh3917", "hero_of_gb", "Rezcwa")

    private fun generatePreTags(name: String): MutableList<Tags> {
        val tags = mutableListOf<Tags>()

        if (name.equals(Config[PlayerName], true)) tags += Tags.You
        if (name.lowercase() == "ambmt") tags += Tags.Ambmt
        if (name.lowercase() in devNames) tags += Tags.VoxylDev
        if (name.lowercase() == "carburettor") tags += Tags.OverlayDev

        return tags
    }

    private fun generatePostTags(player: PlayerState): MutableList<Tags> {
        var tags = mutableListOf<Tags>()

        if (LeaderboardTrackerWhatEvenIsAViewModel.foundInLevelLB(player["uuid"]!!)) tags += Tags.LevelLB
        if (LeaderboardTrackerWhatEvenIsAViewModel.foundInWWLB(player["uuid"]!!)) tags += Tags.WWLB

        if (Tags.LevelLB in tags && Tags.WWLB in tags) tags = mutableListOf(Tags.LevelAndWWLB)
        return tags
    }

    private fun SnapshotStateList<PlayerState>.remove(name: String) {
        _players.remove(PlayerState(name))
    }

    fun refresh(name: String, cs: CoroutineScope) {
        remove(name)
        add(name, cs)
    }

    fun refreshAll(cs: CoroutineScope) {
        val names = _players.map { it.name }
        removeAll()
        names.forEach { add(it, cs) }
    }

    fun remove(name: String) {
        _players.remove(name)
        jobs[name]?.cancel()
        jobs.remove(name)
        HomemadeCache.remove(name)
    }

    fun removeAll() {
        _players.clear()
        jobs.forEach { it.value.cancel() }
        jobs.clear()
        HomemadeCache.clear()
    }
}
