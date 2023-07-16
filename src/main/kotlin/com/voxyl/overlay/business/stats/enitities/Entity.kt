package com.voxyl.overlay.business.stats.enitities

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.voxyl.overlay.business.stats.enitities.types.RawEntity
import com.voxyl.overlay.business.stats.enitities.tags.Tag

class Entity(
    val name: String,
    val isLoading: Boolean = false,
    val raw: RawEntity? = null,
    val error: String = "",
    val tags: SnapshotStateList<Tag> = mutableStateListOf()
) {
    operator fun get(key: String): String? {
        return raw?.get(key)
    }

    override fun equals(other: Any?): Boolean {
        return name.equals((other as? Entity)?.name, true)
    }

    override fun hashCode(): Int {
        return name.lowercase().hashCode()
    }

    companion object {
        val dummy get() = Entity("")
    }
}
