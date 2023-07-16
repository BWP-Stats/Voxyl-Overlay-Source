package com.voxyl.overlay.controllers.common

import androidx.compose.runtime.*
import com.voxyl.overlay.ui.entitystats.EntityStats as PlayerStatsUI
import com.voxyl.overlay.ui.settings.Settings as SettingsUI
import kotlin.properties.Delegates

enum class CurrentScreen(val screen: @Composable () -> Unit) {
    PlayerStats({ PlayerStatsUI() }), Settings({ SettingsUI() });

    companion object {
        private var _current = mutableStateOf(PlayerStats)

        private var subscribers = mutableListOf<(CurrentScreen, CurrentScreen) -> Unit>()

        private var screen by mutableStateOf(PlayerStats.screen)

        var current by Delegates.observable(_current.value) { _, old, new ->
            subscribers.forEach { it(old, new) }
            _current.value = new
            screen = new.screen
        }

        @Composable
        operator fun invoke() {
            screen()
        }

        fun subscribeToChange(action: (CurrentScreen, CurrentScreen) -> Unit) {
            subscribers += action
        }
    }
}
