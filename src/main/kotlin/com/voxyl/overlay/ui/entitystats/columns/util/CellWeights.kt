package com.voxyl.overlay.ui.entitystats.columns.util

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import com.voxyl.overlay.business.stats.enitities.Entity
import com.voxyl.overlay.controllers.playerstats.Entities
import kotlin.math.max

object CellWeights {
    @PublishedApi
    internal val weights = mutableStateMapOf<String, SnapshotStateMap<Entity, Float>>()

    init {
        Entities.addChangeListener { entity, type ->
            if (type == Entities.ChangeType.Remove) {
                weights.values.forEach { it.remove(entity) }
            }
        }
    }

    fun get(dataString: String, default: Number): Float {
        return max(default.toFloat(), (weights[dataString]?.maxOfOrNull { it.value } ?: 0f) / 3f)
    }

    fun put(dataString: String, entity: Entity, weight: Number) {
        weights.getOrPut(dataString, ::SnapshotStateMap)[entity] = weight.toFloat()
    }
}
