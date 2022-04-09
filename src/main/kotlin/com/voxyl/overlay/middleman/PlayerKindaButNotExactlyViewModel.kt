package com.voxyl.overlay.middleman

import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.*
import com.voxyl.overlay.data.player.Status
import com.voxyl.overlay.data.player.Player
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

object PlayerKindaButNotExactlyViewModel {

    private var state = mutableStateListOf<PlayerState>()

    fun add(name: String, cs: CoroutineScope) {

        Player.makePlayer(name).onEach {
            when (it) {
                is Status.Loaded -> {
                    println("got 'loaded' for $name")
                    state.remove(name)
                    state += PlayerState(name, player = it.data)
                }
                is Status.Loading -> {
                    println("got 'loading' for $name")
                    state.remove(name)
                    state += PlayerState(name, isLoading = true)
                }
                is Status.Error -> {
                    println("got 'error' for $name")
                    state.remove(name)
                    state += PlayerState(name, error = it.message ?: "An unexpected error has occurred")
                }
            }
        }.launchIn(cs)
    }

    private fun <T> SnapshotStateList<T>.remove(name: String) {
        state.remove(PlayerState(name))
    }

    fun clear() {
        state.clear()
    }

    fun remove(name: String) {
        state.remove(name)
    }

    fun getPlayers() = state

    fun getPlayersCopy() = state.map{it.copy()}

    override fun toString(): String {
        var str = "";
        state.forEach {
            str += "hi $it"
        }
        return str + " " + state.size
    }
}
