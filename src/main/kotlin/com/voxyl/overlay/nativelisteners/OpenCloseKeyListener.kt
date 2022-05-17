package com.voxyl.overlay.nativelisteners

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener
import com.voxyl.overlay.Window

object OpenCloseKeyListener : NativeKeyListener {
    var paramString by mutableStateOf(
        "NATIVE_KEY_RELEASED,keyCode=43,keyText=Back Slash,keyChar=Undefined,modifiers=Ctrl,keyLocation=KEY_LOCATION_STANDARD,rawCode=220"
    )

    override fun nativeKeyReleased(e: NativeKeyEvent) {
        if (e.paramString() == paramString) {
            Window.isMinimized = !Window.isMinimized
        }
    }
}
