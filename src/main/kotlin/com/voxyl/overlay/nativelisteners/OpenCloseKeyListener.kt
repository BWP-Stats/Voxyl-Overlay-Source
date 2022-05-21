package com.voxyl.overlay.nativelisteners

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener
import com.voxyl.overlay.Window
import com.voxyl.overlay.settings.config.Config
import com.voxyl.overlay.settings.config.ConfigKeys.OpenAndCloseKeybind
import com.voxyl.overlay.settings.window.SavedWindowState
import com.voxyl.overlay.settings.window.SavedWindowStateKeys.IsAlwaysOnTop
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object OpenCloseKeyListener : NativeKeyListener {
    var paramString by mutableStateOf(
        Config[OpenAndCloseKeybind]
    )

    override fun nativeKeyReleased(e: NativeKeyEvent) {
        if (e.paramString() == paramString) {
            if (Window.isMinimized) {
                Window.focusableWindowState = false
                Window.isMinimized = false
                Window.focusableWindowState = true
            } else {
                Window.isMinimized = true
            }
        }
    }
}
