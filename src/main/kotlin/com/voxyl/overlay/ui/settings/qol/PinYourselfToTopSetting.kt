package com.voxyl.overlay.ui.settings.qol

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.voxyl.overlay.settings.config.Config
import com.voxyl.overlay.settings.config.ConfigKeys.PinYourselfToTop
import com.voxyl.overlay.ui.common.elements.MyCheckbox
import com.voxyl.overlay.ui.theme.VText

@Composable
fun PinYourselfToTopCheckbox(addYourself: MutableState<Boolean>, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .height(44.dp)
            .fillMaxWidth()
            .padding(horizontal = 30.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        var checked by remember {
            mutableStateOf(Config[PinYourselfToTop].toBooleanStrictOrNull() ?: false)
        }

        MyCheckbox(
            checked = checked && addYourself.value,
            onCheckedChange = {
                if (it) {
                    Config[PinYourselfToTop] = "true"
                    checked = it
                } else {
                    Config[PinYourselfToTop] = "false"
                    checked = it
                }
            },
            modifier = Modifier.size(12.dp),
            enabled = addYourself.value
        )

        Spacer(modifier = Modifier.width(16.dp))

        getPinYourselfText(addYourself)
    }
}

@Composable
private fun getPinYourselfText(addYourself: MutableState<Boolean>) {
    if (addYourself.value) {
        VText("Pin yourself to the top at all times")
    } else {
        VText("Pin yourself to the top at all times (Must have the 'add yourself' setting checked'", alpha = 0.5f)
    }
}