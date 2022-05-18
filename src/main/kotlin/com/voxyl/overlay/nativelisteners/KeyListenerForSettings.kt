package com.voxyl.overlay.nativelisteners

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener
import io.github.aakira.napier.Napier
import kotlinx.coroutines.delay

object KeyListenerForSettings : NativeKeyListener {
    private var paramString: String? = null
    private var waiting = false

    suspend fun awaitParamString(): String {
        return try {
            NativeListeners.add(KeyListenerForSettings)
            paramString = null
            while (paramString == null) {
                delay(100)
            }
            NativeListeners.remove(KeyListenerForSettings)
            paramString!!
        } catch (e: Exception) {
            NativeListeners.remove(KeyListenerForSettings)
            Napier.wtf(e) { "Failed to await paramString" }
            "Select a key"
        }
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