package com.voxyl.overlay.business.playerfetching.player

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.voxyl.overlay.business.playerfetching.player.tags.Tag

data class PlayerState(
    val name: String,
    val isLoading: Boolean = false,
    val player: StatefulEntity? = null,
    val error: String = "",
    val tags: SnapshotStateList<Tag> = mutableStateListOf()
) {
    operator fun get(key: String): String? {
        return player?.get(key)
    }

    override fun equals(other: Any?): Boolean {
        return name.equals((other as? PlayerState)?.name, true)
    }

    override fun hashCode(): Int {
        return name.lowercase().hashCode()
    }

    companion object {
        val empty get() = PlayerState("")
    }
}
