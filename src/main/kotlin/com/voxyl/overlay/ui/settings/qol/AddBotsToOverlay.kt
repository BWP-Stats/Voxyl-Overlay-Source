package com.voxyl.overlay.ui.settings.qol

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.voxyl.overlay.business.settings.config.AddBotsToOverlay
import com.voxyl.overlay.business.settings.config.AddYourselfToOverlay
import com.voxyl.overlay.business.settings.config.Config
import com.voxyl.overlay.ui.elements.VCheckbox
import com.voxyl.overlay.ui.elements.VText

@Composable
fun AddBotsToOverlayCheckbox(addBots: MutableState<Boolean>, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .height(44.dp)
            .fillMaxWidth()
            .padding(horizontal = 30.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        VCheckbox(
            checked = addBots.value,
            onCheckedChange = {
                if (it) {
                    Config[AddBotsToOverlay] = "true"
                    addBots.value = it
                } else {
                    Config[AddBotsToOverlay] = "false"
                    addBots.value = it
                }
            },
            modifier = Modifier.size(12.dp)
        )

        Spacer(modifier = Modifier.size(16.dp))

        VText("Add bots to overlay")
    }
}
