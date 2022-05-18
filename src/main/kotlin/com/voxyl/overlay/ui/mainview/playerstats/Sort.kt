package com.voxyl.overlay.ui.mainview.playerstats

import androidx.compose.runtime.mutableStateOf
import com.voxyl.overlay.data.player.PlayerState
import com.voxyl.overlay.settings.config.Config
import com.voxyl.overlay.settings.config.ConfigKeys.SortBy
import com.voxyl.overlay.settings.config.ConfigKeys.SortAsc

object Sort {
    private var _by = mutableStateOf(Config[SortBy])

    var by
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

    private var _ascending = mutableStateOf(Config[SortAsc].toBooleanStrictOrNull() ?: false)

    var ascending
        get() = _ascending.value
        set(value) {
            _ascending.value = value
            Config[SortAsc] = value.toString()
        }

    fun sortPlayersList(players: List<PlayerState>): List<PlayerState> {
        return if (players.any { Sort.by != "name" }) {
            if (Sort.ascending) {
                players.sortedBy {
                    if (it.error.isNotBlank()) Int.MAX_VALUE else it[Sort.by]?.toInt()
                }
            } else {
                players.sortedByDescending {
                    if (it.error.isNotBlank()) 0 else it[Sort.by]?.toInt()
                }
            }
        } else {
            if (Sort.ascending) {
                players.sortedBy {
                    if (it.error.isNotBlank()) "∐∐∐∐∐∐∐∐∐∐∐∐∐∐∐∐∐∐∐∐∐∐∐∐∐∐∐∐∐" else it[Sort.by]
                }
            } else {
                players.sortedByDescending {
                    if (it.error.isNotBlank()) "                             " else it[Sort.by]?.trimStart('_')
                }
            }
        }
    }
}