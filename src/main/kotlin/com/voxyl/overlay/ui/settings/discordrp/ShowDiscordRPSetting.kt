package com.voxyl.overlay.ui.settings.discordrp

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.voxyl.overlay.business.settings.config.Config
import com.voxyl.overlay.business.settings.config.ShowDiscordRP
import com.voxyl.overlay.ui.elements.VCheckbox
import com.voxyl.overlay.ui.elements.VText

@Composable
fun ShowDiscordRPCheckbox(modifier: Modifier = Modifier) {
    var showDiscordRP by remember { mutableStateOf(Config[ShowDiscordRP] != "false") }

    Row(
        modifier = modifier
            .height(44.dp)
            .fillMaxWidth()
            .padding(horizontal = 30.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        VCheckbox(
            checked = showDiscordRP,
            onCheckedChange = {
                if (it) {
                    Config[ShowDiscordRP] = "true"
                    showDiscordRP = true
                } else {
                    Config[ShowDiscordRP] = "false"
                    showDiscordRP = false
                }
            },
            modifier = Modifier.size(12.dp)
        )

        Spacer(modifier = Modifier.size(16.dp))

        VText("Show Discord Rich Presence (Restart required to apply)")
    }
}
