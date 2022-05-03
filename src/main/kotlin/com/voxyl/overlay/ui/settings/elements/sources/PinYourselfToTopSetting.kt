package com.voxyl.overlay.ui.settings.elements.sources

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.voxyl.overlay.config.Config
import com.voxyl.overlay.config.ConfigKeys.PIN_YOURSELF_TO_TOP
import com.voxyl.overlay.ui.common.elements.MyCheckbox
import com.voxyl.overlay.ui.theme.MainWhite
import com.voxyl.overlay.ui.common.elements.MyText

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
            mutableStateOf(Config[PIN_YOURSELF_TO_TOP].toBooleanStrictOrNull() ?: false)
        }

        MyCheckbox(
            checked = checked && addYourself.value,
            onCheckedChange = {
                if (it) {
                    Config[PIN_YOURSELF_TO_TOP] = "true"
                    checked = it
                } else {
                    Config[PIN_YOURSELF_TO_TOP] = "false"
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
        MyText("Pin yourself to the top at all times", color = MainWhite, fontSize = 12.sp)
    } else {
        MyText("Pin yourself to the top at all times (Must have the 'add yourself' setting checked'", color = MainWhite.copy(alpha = 0.5f), fontSize = 12.sp)
    }
}