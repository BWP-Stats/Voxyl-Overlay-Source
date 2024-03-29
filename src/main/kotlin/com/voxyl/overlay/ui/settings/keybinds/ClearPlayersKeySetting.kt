package com.voxyl.overlay.ui.settings.keybinds

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.voxyl.overlay.business.nativelisteners.ClearPlayersKeyListener
import com.voxyl.overlay.business.nativelisteners.KeyListenerForSettings
import com.voxyl.overlay.business.nativelisteners.NativeUtil.toCleanKeyCodeString
import com.voxyl.overlay.business.settings.config.ClearPlayersKeybind
import com.voxyl.overlay.business.settings.config.Config
import com.voxyl.overlay.controllers.common.ui.am
import com.voxyl.overlay.ui.elements.VText
import kotlinx.coroutines.launch

@Composable
fun ClearPlayersKeySetting(modifier: Modifier = Modifier) {
    val cs = rememberCoroutineScope()

    Row(
        modifier = modifier
            .height(32.dp)
            .fillMaxWidth()
            .padding(horizontal = 19.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.size(8.dp))

        RefreshPlayersKeyText()

        Box(
            modifier = Modifier
                .background(Color(0f, 0f, 0f, .3f).am)
                .clickable {
                    if (ClearPlayersKeyListener.paramString == "Press 'esc' to cancel") return@clickable

                    ClearPlayersKeyListener.paramString = "Press 'esc' to cancel"

                    cs.launch {
                        ClearPlayersKeyListener.paramString = KeyListenerForSettings.awaitParamString()
                        Config[ClearPlayersKeybind] = ClearPlayersKeyListener.paramString
                    }
                }
                .fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {
            VText(
                text = ClearPlayersKeyListener.paramString.toCleanKeyCodeString(),
                modifier = Modifier.padding(horizontal = 10.dp)
            )
        }
    }
}

@Composable
private fun RefreshPlayersKeyText() {
    Box(Modifier.width(240.dp)) {
        VText("Keybind to clear all players in overlay")
    }
}
