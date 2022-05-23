package com.voxyl.overlay.business.nativelisteners

import com.github.kwhat.jnativehook.GlobalScreen
import com.github.kwhat.jnativehook.NativeHookException
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener
import io.github.aakira.napier.Napier

object NativeListeners {
    fun initialize() {
        try {
            GlobalScreen.registerNativeHook()
            GlobalScreen.addNativeKeyListener(OpenCloseKeyListener)
            GlobalScreen.addNativeKeyListener(ClearPlayersKeyListener)
            GlobalScreen.addNativeKeyListener(RefreshPlayersKeyListener)
        } catch (e: NativeHookException) {
            Napier.e(e) { "Failed to register native hooks" }
        }
    }

    fun add(listener: NativeKeyListener) {
        GlobalScreen.addNativeKeyListener(listener)
    }

    fun remove(listener: NativeKeyListener) {
        GlobalScreen.removeNativeKeyListener(listener)
    }
}
