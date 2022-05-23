package com.voxyl.overlay.business.nativelisteners

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener
import com.voxyl.overlay.Window
import com.voxyl.overlay.kindasortasomewhatviewmodelsishiguessithinkidkwhatevericantbebotheredsmh.PlayerKindaButNotExactlyViewModel
import com.voxyl.overlay.settings.config.Config
import com.voxyl.overlay.settings.config.ConfigKeys.RefreshPlayersKeybind
import kotlinx.coroutines.GlobalScope

object RefreshPlayersKeyListener : NativeKeyListener {
    var paramString by mutableStateOf(
        Config[RefreshPlayersKeybind]
    )

    override fun nativeKeyReleased(e: NativeKeyEvent) {
        if (e.paramString() == paramString) {
            PlayerKindaButNotExactlyViewModel.refreshAll(GlobalScope)
        }
    }
}
