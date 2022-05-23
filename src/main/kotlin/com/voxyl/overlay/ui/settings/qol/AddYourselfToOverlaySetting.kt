package com.voxyl.overlay.ui.settings.qol

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.voxyl.overlay.settings.config.Config
import com.voxyl.overlay.settings.config.ConfigKeys.AddYourselfToOverlay
import com.voxyl.overlay.ui.elements.MyCheckbox
import com.voxyl.overlay.ui.theme.VText

@Composable
fun AddYourselfToOverlayCheckbox(addYourself: MutableState<Boolean>, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .height(44.dp)
            .fillMaxWidth()
            .padding(horizontal = 30.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        MyCheckbox(
            checked = addYourself.value,
            onCheckedChange = {
                if (it) {
                    Config[AddYourselfToOverlay] = "true"
                    addYourself.value = it
                } else {
                    Config[AddYourselfToOverlay] = "false"
                    addYourself.value = it
                }
            },
            modifier = Modifier.size(12.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        VText("Add yourself to overlay")
    }
}