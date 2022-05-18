package com.voxyl.overlay.ui.mainview.playerstats

import androidx.compose.runtime.mutableStateOf
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
}