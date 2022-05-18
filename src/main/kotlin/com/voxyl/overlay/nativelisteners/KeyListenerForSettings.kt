package com.voxyl.overlay.nativelisteners

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener
import kotlinx.coroutines.delay

object KeyListenerForSettings : NativeKeyListener {
    private var paramString: String? = null
    private var waiting = false

    suspend fun awaitParamString(): String {
        NativeListeners.add(KeyListenerForSettings)
        paramString = null
        while(paramString == null) {
            delay(100)
        }
        NativeListeners.remove(KeyListenerForSettings)
        return paramString!!
    }

    override fun nativeKeyReleased(e: NativeKeyEvent) {
        if (paramString == null) {
            paramString = if (e.keyCode == NativeKeyEvent.VC_ESCAPE) {
                "Select a key"
            } else {
                e.paramString()
            }
        }
    }
}