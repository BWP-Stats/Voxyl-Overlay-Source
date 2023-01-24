package com.voxyl.overlay.business.nativelisteners

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener
import com.voxyl.overlay.AppWindow
import com.voxyl.overlay.business.settings.config.Config
import com.voxyl.overlay.business.settings.config.OpenAndCloseKeybind

object OpenCloseKeyListener : NativeKeyListener {
    var paramString by mutableStateOf(
        Config[OpenAndCloseKeybind]
    )

    override fun nativeKeyReleased(e: NativeKeyEvent) {
        if (e.paramString() == paramString) {
            if (AppWindow.isMinimized) {
                AppWindow.focusableWindowState = false
                AppWindow.isMinimized = false
                AppWindow.focusableWindowState = true
            } else {
                AppWindow.isMinimized = true
            }
        }
    }
}
