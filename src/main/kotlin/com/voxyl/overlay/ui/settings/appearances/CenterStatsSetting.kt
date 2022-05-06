package com.voxyl.overlay.ui.settings.appearances

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.voxyl.overlay.settings.config.Config
import com.voxyl.overlay.settings.config.ConfigKeys.CenterStats
import com.voxyl.overlay.ui.common.elements.MyCheckbox
import com.voxyl.overlay.ui.common.elements.MyText
import com.voxyl.overlay.ui.theme.MainWhite

@Composable
fun CenterStatsCheckBox(modifier: Modifier = Modifier) {
    var centerStatsHeader by remember { mutableStateOf(Config[CenterStats].toBooleanStrictOrNull() ?: true) }

    Row(
        modifier = modifier
            .height(44.dp)
            .fillMaxWidth()
            .padding(horizontal = 30.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        MyCheckbox(
            checked = centerStatsHeader,
            onCheckedChange = {
                if (it) {
                    Config[CenterStats] = "true"
                    centerStatsHeader = true
                } else {
                    Config[CenterStats] = "false"
                    centerStatsHeader = false
                }
            },
            modifier = Modifier.size(12.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        MyText("Center the stats in the columns", color = MainWhite, fontSize = 12.sp)
    }
}
