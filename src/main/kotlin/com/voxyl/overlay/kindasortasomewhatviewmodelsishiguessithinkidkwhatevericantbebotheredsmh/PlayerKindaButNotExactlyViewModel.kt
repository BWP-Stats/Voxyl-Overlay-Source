package com.voxyl.overlay.kindasortasomewhatviewmodelsishiguessithinkidkwhatevericantbebotheredsmh

import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.*
import com.voxyl.overlay.business.playerfetching.player.*
import com.voxyl.overlay.business.playerfetching.player.tags.*
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
            val tags2 = (listOf(*tags) + TagGenerator.generatePreTags(name, rawName))
                .distinctBy { tag -> tag.javaClass }
                .toMutableStateList()

            jobs[name] = PlayerFactory.makePlayer(name).onEach {
                _players += when (it) {
                    is ResponseStatus.Loaded -> {
                        _players.remove(name)
                        PlayerState(
                            name = (it.data as Player)["name"] ?: name,
                            player = it.data,
                            tags = tags2
                        ).also { ps ->
                            try {
                                ps.tags += TagGenerator.generatePostTags(ps).distinctBy { tag -> tag.javaClass }
                            } catch (e: Exception) {
                                Napier.wtf(e) { "Failed to generate post tags" }
                            }
                        }
                    }
                    is ResponseStatus.Loading -> {
                        _players.remove(name)
                        PlayerState(name = name, isLoading = true, tags = tags2)
                    }
                    is ResponseStatus.Error -> {
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
            Napier.wtf(e) { "Failed to add player $name (in PVM)" }
        }
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
