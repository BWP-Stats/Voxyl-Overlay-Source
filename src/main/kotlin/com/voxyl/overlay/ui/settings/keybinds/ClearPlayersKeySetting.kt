package com.voxyl.overlay.ui.settings.keybinds

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.voxyl.overlay.nativelisteners.ClearPlayersKeyListener
import com.voxyl.overlay.nativelisteners.KeyListenerForSettings
import com.voxyl.overlay.nativelisteners.NativeUtil.toCleanKeyCodeString
import com.voxyl.overlay.ui.theme.VText
import com.voxyl.overlay.ui.theme.am

@Composable
fun ClearPlayersKeySetting(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .height(32.dp)
            .fillMaxWidth()
            .padding(horizontal = 19.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(8.dp))

        ClearPlayersKeyText()

        Spacer(modifier = Modifier.width(10.dp))

        VText(
            text = ClearPlayersKeyListener.paramString.toCleanKeyCodeString(),
            modifier = Modifier
                .background(Color(0f, 0f, 0f, .3f).am)
                .clickable {
                    ClearPlayersKeyListener.paramString = "Press 'esc' to cancel"
                    ClearPlayersKeyListener.paramString = KeyListenerForSettings.awaitParamString()
                }
                .padding(10.dp)
        )
    }
}

@Composable
private fun ClearPlayersKeyText() {
    VText("Keybind to clear all players in overlay ")
    Spacer(modifier = Modifier.width(16.dp))
}