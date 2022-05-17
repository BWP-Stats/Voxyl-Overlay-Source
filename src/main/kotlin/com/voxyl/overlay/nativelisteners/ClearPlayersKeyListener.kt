package com.voxyl.overlay.nativelisteners

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener
import com.voxyl.overlay.middleman.PlayerKindaButNotExactlyViewModel
import kotlin.system.measureTimeMillis

object ClearPlayersKeyListener : NativeKeyListener {
    var paramString by mutableStateOf(
        "NATIVE_KEY_RELEASED,keyCode=27,keyText=Close Bracket,keyChar=Undefined,modifiers=Ctrl,keyLocation=KEY_LOCATION_STANDARD,rawCode=221"
    )

    override fun nativeKeyReleased(e: NativeKeyEvent) {
        if (e.paramString() == paramString) {
            PlayerKindaButNotExactlyViewModel.removeAll()
        }
    }
}