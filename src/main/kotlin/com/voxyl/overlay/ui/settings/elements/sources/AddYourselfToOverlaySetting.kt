package com.voxyl.overlay.ui.settings.elements.sources

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.voxyl.overlay.config.Config
import com.voxyl.overlay.config.ConfigKeys.AddYourselfToOverlay
import com.voxyl.overlay.ui.common.elements.MyCheckbox
import com.voxyl.overlay.ui.theme.MainWhite
import com.voxyl.overlay.ui.common.elements.MyText

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

        MyText("Add yourself to overlay", color = MainWhite, fontSize = 12.sp)
    }
}