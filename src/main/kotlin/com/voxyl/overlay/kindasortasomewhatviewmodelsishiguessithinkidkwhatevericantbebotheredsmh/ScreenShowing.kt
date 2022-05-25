package com.voxyl.overlay.kindasortasomewhatviewmodelsishiguessithinkidkwhatevericantbebotheredsmh

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.voxyl.overlay.ui.playerstats.playerstats.PlayerStats
import com.voxyl.overlay.ui.settings.Settings

object ScreenShowing {
    private val _screenId = mutableStateOf("playerstats")

    var screenId: String
        get() = _screenId.value
        set(value) = when (value) {
            "playerstats" -> {
                _screenId.value = value
                screen = { PlayerStats() }
            }
            "settings" -> {
                _screenId.value = value
                screen = { Settings() }
            }
            else -> {}
        }


    var screen by mutableStateOf<@Composable () -> Unit>({ PlayerStats() })
        private set
}