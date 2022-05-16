package com.voxyl.overlay.data.player

data class PlayerState(
    val name: String,
    val isLoading: Boolean = false,
    val player: Player? = null,
    val error: String = ""
) {
    operator fun get(key: String): String? = player?.stats?.get(key)
    override fun equals(other: Any?) = name == (other as PlayerState).name
    override fun hashCode() = name.hashCode()
}
