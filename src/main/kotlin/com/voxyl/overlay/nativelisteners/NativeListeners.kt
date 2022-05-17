package com.voxyl.overlay.nativelisteners

import com.github.kwhat.jnativehook.GlobalScreen
import com.github.kwhat.jnativehook.NativeHookException
import io.github.aakira.napier.Napier

object NativeListeners {
    fun initialize() {
        try {
            GlobalScreen.registerNativeHook()
            GlobalScreen.addNativeKeyListener(OpenCloseKeyListener)
            GlobalScreen.addNativeKeyListener(ClearPlayersKeyListener)
            GlobalScreen.addNativeKeyListener(KeyListenerForSettings)
        } catch (e: NativeHookException) {
            Napier.e(e) { "Failed to register native hooks" }
        }
    }
}
