package com.voxyl.overlay.kindasortasomewhatviewmodelsishiguessithinkidkwhatevericantbebotheredsmh

import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.*
import com.voxyl.overlay.business.networking.player.*
import com.voxyl.overlay.business.networking.player.tags.*
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

    fun add(rawName: String, cs: CoroutineScope, vararg tags: Tag) {

        val name = if (
            Config["aliases"]?.lowercase()?.split(",")
                ?.contains(rawName.lowercase()) == true && Config["show_your_stats_instead_of_aliases"] == "true"
        ) Config[PlayerName] else rawName

        try {
            val tags2 = (listOf(*tags) + generatePreTags(name, rawName))
                .distinctBy { tag -> tag.javaClass }
                .toMutableStateList()

            jobs[name] = PlayerFactory.makePlayer(name).onEach {
                _players += when (it) {
                    is Status.Loaded -> {
                        _players.remove(name)
                        PlayerState(
                            name = (it.data as Player)["name"] ?: name,
                            player = it.data,
                            tags = tags2
                        ).also { ps ->
                            try {
                                ps.tags += generatePostTags(ps).distinctBy { tag -> tag.javaClass }
                            } catch (e: Exception) {
                                Napier.wtf(e) { "Failed to generate post tags" }
                            }
                        }
                    }
                    is Status.Loading -> {
                        _players.remove(name)
                        println(jobs.size)
                        PlayerState(name = name, isLoading = true, tags = tags2)
                    }
                    is Status.Error -> {
                        _players.remove(name)
                        PlayerState(
                            name = name,
                            error = it.message ?: "An unexpected error has occurred",
                            tags = (tags2 + Error).distinctBy { tag -> tag.javaClass }.toMutableStateList()
                        )
                    }
                }
            }.launchIn(cs)
        } catch (e: Exception) {
            jobs[name]?.cancel()
            _players.remove(name)
            Napier.wtf(e) { "Failed to add player $name" }
        }
    }

    private val devNames = listOf("ambmt", "_lightninq", "vitroid", "firestarad", "sirjosh3917", "hero_of_gb", "rezcwa")

    private fun generatePreTags(name: String, rawName: String): MutableList<Tag> {
        val tags = mutableListOf<Tag>()

        if (
            name.equals(Config[PlayerName], true) || Config["aliases"]?.lowercase()?.split(",")
                ?.contains(rawName.lowercase()) == true
        ) {
            tags += You
        }
        if (name.lowercase() == "ambmt") tags += Ambmt
        if (name.lowercase() in devNames) tags += VoxylDev
        if (name.lowercase() == "carburettor") tags += OverlayDev

        return tags
    }

    private fun generatePostTags(player: PlayerState): MutableList<Tag> {
        var tags = mutableListOf<Tag>()

        val lvlLbPos = LeaderboardTrackerWhatEvenIsAViewModel.findInLevelLB(player["uuid"]!!)
        val wwLbPos = LeaderboardTrackerWhatEvenIsAViewModel.findInWWLB(player["uuid"]!!)

        if (lvlLbPos != null) tags += LevelLB(lvlLbPos["position"].asString ?: "")
        if (wwLbPos != null) tags += LevelLB(wwLbPos["position"].asString ?: "")

        if (tags.size == 2) tags = mutableListOf(
            LevelAndWWLB(
                lvlLbPos?.get("position")?.asString ?: "",
                wwLbPos?.get("position")?.asString ?: ""
            )
        )
        return tags
    }

    private fun SnapshotStateList<PlayerState>.remove(name: String) {
        _players.remove(PlayerState(name))
    }

    fun refreshAll(cs: CoroutineScope) {
        val players = _players.map {
            it.name to it.tags.toList()
        }
        removeAll()
        players.forEach {
            add(it.first, cs, *it.second.toTypedArray())
        }
    }

    fun remove(name: String) {
        jobs[name]?.cancel()
        jobs.remove(name)
        _players.remove(name)
    }

    fun removeAll() {
        jobs.forEach { it.value.cancel() }
        jobs.clear()
        _players.clear()
    }
}
