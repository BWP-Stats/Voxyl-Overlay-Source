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
import com.voxyl.overlay.business.nativelisteners.KeyListenerForSettings
import com.voxyl.overlay.business.nativelisteners.NativeUtil.toCleanKeyCodeString
import com.voxyl.overlay.business.nativelisteners.OpenCloseKeyListener
import com.voxyl.overlay.settings.config.Config
import com.voxyl.overlay.settings.config.ConfigKeys.OpenAndCloseKeybind
import com.voxyl.overlay.ui.elements.VText
import com.voxyl.overlay.ui.theme.am
import kotlinx.coroutines.launch

@Composable
fun OpenCloseKeySetting(modifier: Modifier = Modifier) {
    val cs = rememberCoroutineScope()

    Row(
        modifier = modifier
            .height(32.dp)
            .fillMaxWidth()
            .padding(horizontal = 19.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.size(8.dp))

        OpenCloseKeyText()

        Box(
            modifier = Modifier
                .background(Color(0f, 0f, 0f, .3f).am)
                .clickable {
                    if (OpenCloseKeyListener.paramString == "Press 'esc' to cancel") return@clickable

                    OpenCloseKeyListener.paramString = "Press 'esc' to cancel"

                    cs.launch {
                        OpenCloseKeyListener.paramString = KeyListenerForSettings.awaitParamString()
                        Config[OpenAndCloseKeybind] = OpenCloseKeyListener.paramString
                    }
                }
                .fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {
            VText(
                text = OpenCloseKeyListener.paramString.toCleanKeyCodeString(),
                modifier = Modifier.padding(horizontal = 10.dp)
            )
        }
    }
}

@Composable
private fun OpenCloseKeyText() {
    Box(Modifier.width(240.dp)) {
        VText("Keybind to minimize/maximize the overlay")
    }
}