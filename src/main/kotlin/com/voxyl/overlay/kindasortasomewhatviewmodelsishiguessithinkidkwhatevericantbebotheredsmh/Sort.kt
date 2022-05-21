package com.voxyl.overlay.kindasortasomewhatviewmodelsishiguessithinkidkwhatevericantbebotheredsmh

import androidx.compose.runtime.mutableStateOf
import com.voxyl.overlay.dataslashbusiness.player.PlayerState
import com.voxyl.overlay.settings.config.Config
import com.voxyl.overlay.settings.config.ConfigKeys.SortAsc
import com.voxyl.overlay.settings.config.ConfigKeys.SortBy

object Sort {
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

    private var _ascending = mutableStateOf(Config[SortAsc].toBooleanStrictOrNull() ?: false)

    var ascending
        get() = _ascending.value
        set(value) {
            _ascending.value = value
            Config[SortAsc] = value.toString()
        }

    fun sortPlayersList(players: List<PlayerState>): List<PlayerState> {
        return if (players.any { by != "name" }) {
            if (ascending) {
                players.sortedBy {
                    if (it.error.isNotBlank()) Double.MAX_VALUE else it[by]?.toDouble()
                }
            } else {
                players.sortedByDescending {
                    if (it.error.isNotBlank()) 0.0 else it[by]?.toDouble()
                }
            }
        } else {
            if (ascending) {
                players.sortedBy {
                    if (it.error.isNotBlank()) "∐∐∐∐∐∐∐∐∐∐∐∐∐∐∐∐∐∐∐∐∐∐∐∐∐∐∐∐∐" else it[by]
                }
            } else {
                players.sortedByDescending {
                    if (it.error.isNotBlank()) "                             " else it[by]?.trimStart('_')
                }
            }
        }
    }
}