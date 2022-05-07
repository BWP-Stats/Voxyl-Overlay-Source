package com.voxyl.overlay.middleman

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.*
import com.voxyl.overlay.data.homemadesimplecache.HomemadeCache
import com.voxyl.overlay.data.player.Status
import com.voxyl.overlay.data.player.Player
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

object PlayerKindaButNotExactlyViewModel {

    private var _players = mutableStateListOf<PlayerState>()

    val players: SnapshotStateList<PlayerState>
        get() = _players

    private var jobs = mutableStateListOf<Job>()

    fun add(name: String, cs: CoroutineScope) {
        jobs += Player.makePlayer(name).onEach {
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

    fun clear() {
        _players.clear()
        jobs.forEach { it.cancel() }
        jobs.clear()
    }

    fun refreshAll(cs: CoroutineScope) {
        val names = _players.map { it.name }
        clear()
        names.forEach { add(it, cs) }
    }

    fun remove(name: String) {
        _players.remove(name)
    }

    override fun toString(): String {
        var str = "";
        _players.forEach {
            str += "hi $it"
        }
        return str + " " + _players.size
    }
}
