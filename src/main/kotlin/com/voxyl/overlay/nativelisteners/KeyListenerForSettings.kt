package com.voxyl.overlay.nativelisteners

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener

object KeyListenerForSettings : NativeKeyListener {
    private var paramString: String? = null

    fun awaitParamString(): String {
        paramString = null
        while(paramString == null) {
            Thread.sleep(100)
        }
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