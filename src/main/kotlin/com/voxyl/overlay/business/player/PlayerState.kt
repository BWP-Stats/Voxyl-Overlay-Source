package com.voxyl.overlay.business.player

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.voxyl.overlay.business.player.tags.Tag

data class PlayerState(
    val name: String,
    val isLoading: Boolean = false,
    val player: Player? = null,
    val error: String = "",
    val tags: SnapshotStateList<Tag> = mutableStateListOf()
) {
    operator fun get(key: String): String? = player?.stats?.get(key)
    override fun equals(other: Any?) = name.equals((other as? PlayerState)?.name, true)
    override fun hashCode() = name.lowercase().hashCode()

    companion object {
        val empty = PlayerState("")
    }
}
