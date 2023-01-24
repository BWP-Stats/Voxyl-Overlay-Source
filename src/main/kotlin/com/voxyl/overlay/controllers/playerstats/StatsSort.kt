package com.voxyl.overlay.controllers.playerstats

import androidx.compose.runtime.mutableStateOf
import com.voxyl.overlay.business.statsfetching.enitities.Entity
import com.voxyl.overlay.business.settings.config.Config
import com.voxyl.overlay.business.settings.config.SortAsc
import com.voxyl.overlay.business.settings.config.SortBy

object StatsSort {
    private var _by = mutableStateOf(Config[SortBy])

    var by: String
        get() = _by.value
        set(value) {
            if (_by.value == value) {
                ascending = !ascending
            } else {
                _by.value = value
                Config[SortBy] = value
                ascending = false
            }
        }

    private var _ascending = mutableStateOf(Config[SortAsc] == "true")

    var ascending
        get() = _ascending.value
        set(value) {
            _ascending.value = value
            Config[SortAsc] = value.toString()
        }

    fun sortEntitiesList(entities: List<Entity>): List<Entity> {
        return if (entities.any { by != "name" }) {
            if (ascending) {
                entities.sortedBy {
                    if (it.error.isNotBlank()) Double.MAX_VALUE else it[by]?.toDouble()
                }
            } else {
                entities.sortedByDescending {
                    if (it.error.isNotBlank()) 0.0 else it[by]?.toDouble()
                }
            }
        } else {
            if (ascending) {
                entities.sortedBy {
                    if (it.error.isNotBlank()) "∐∐∐∐∐∐∐∐∐∐∐∐∐∐∐∐∐∐∐∐∐∐∐∐∐∐∐∐∐" else it[by]
                }
            } else {
                entities.sortedByDescending {
                    if (it.error.isNotBlank()) "                             " else it[by]?.trimStart('_')
                }
            }
        }
    }
}
