package com.voxyl.overlay.middleman

import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.*
import com.voxyl.overlay.data.homemadesimplecache.HomemadeCache
import com.voxyl.overlay.data.player.Status
import com.voxyl.overlay.data.player.Player
import com.voxyl.overlay.data.player.PlayerState
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

object PlayerKindaButNotExactlyViewModel {

    private var _players = mutableStateListOf<PlayerState>()

    val players: SnapshotStateList<PlayerState>
        get() = _players

    private var jobs = mutableMapOf<String, Job>()

    fun add(name: String, cs: CoroutineScope) {
        jobs[name] = PlayerFactory.makePlayer(name).onEach {
            _players += when (it) {
                is Status.Loaded -> {
                    _players.remove(name)
                    PlayerState(
                        name = (it.data as Player)["name"] ?: name,
                        player = it.data
                    ).also { ps ->
                        HomemadeCache.add(ps)
                    }
                }
                is Status.Loading -> {
                    _players.remove(name)
                    PlayerState(name, isLoading = true)
                }
                is Status.Error -> {
                    _players.remove(name)
                    PlayerState(name, error = it.message ?: "An unexpected error has occurred").also { ps ->
                        HomemadeCache.add(ps)
                    }
                }
            }
        }.launchIn(cs)
    }

    private fun <T> SnapshotStateList<T>.remove(name: String) {
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

    override fun toString(): String {
        var str = "";
        _players.forEach {
            str += "hi $it"
        }
        return str + " " + _players.size
    }
}
