package com.voxyl.overlay.business.nativelisteners

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener
import com.voxyl.overlay.business.settings.config.Config
import com.voxyl.overlay.business.settings.config.RefreshPlayersKeybind
import com.voxyl.overlay.controllers.playerstats.Entities
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope

object RefreshPlayersKeyListener : NativeKeyListener {
    var paramString by mutableStateOf(
        Config[RefreshPlayersKeybind]
    )

    @OptIn(DelicateCoroutinesApi::class)
    override fun nativeKeyReleased(e: NativeKeyEvent) {
        if (e.paramString() == paramString) {
            Entities.refreshAll(GlobalScope)
        }
    }
}
