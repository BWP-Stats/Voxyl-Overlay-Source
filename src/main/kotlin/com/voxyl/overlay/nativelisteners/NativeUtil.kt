package com.voxyl.overlay.nativelisteners

import com.github.kwhat.jnativehook.NativeInputEvent
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent

object NativeUtil {
    fun ctrlPressed(e: NativeKeyEvent): Boolean {
        return e.modifiers and NativeInputEvent.CTRL_L_MASK == NativeInputEvent.CTRL_L_MASK
    }

    //NATIVE_KEY_RELEASED,keyCode=27,keyText=Close Bracket,keyChar=Undefined,modifiers=Shift+Ctrl,keyLocation=KEY_LOCATION_STANDARD,rawCode=221
    fun String.toCleanKeyCodeString(): String {
        return this.split(",")
            .associate { it.substringBefore("=") to it.substringAfter("=") }
            .run {
                this["modifiers"]?.let {
                    this["modifiers"]?.replace("+", " + ") + " + " + this["keyText"]
                } ?: this["keyText"] ?: "Set a keybind"
            }
    }
}