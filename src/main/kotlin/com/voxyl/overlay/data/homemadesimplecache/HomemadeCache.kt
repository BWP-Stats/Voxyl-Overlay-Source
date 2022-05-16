package com.voxyl.overlay.data.homemadesimplecache

import com.voxyl.overlay.data.player.PlayerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object HomemadeCache {
    private val cache = mutableMapOf<String, PlayerState>()
        get() = synchronized(this) {
            field
        }

    fun add(player: PlayerState) {
        cache[player.name] = player
    }

    operator fun get(name: String): PlayerState? {
        return cache[name]
    }

    operator fun set(name: String, player: PlayerState) {
        cache[name] = player
    }

    fun remove(name: String) {
        cache.remove(name)
    }

    fun clear() {
        cache.clear()
    }

    fun startAutoClear(cs: CoroutineScope, delayBetweenClears: Long) {
        cs.launch {
            while (true) {
                delay(delayBetweenClears)
                clear()
            }
        }
    }
}