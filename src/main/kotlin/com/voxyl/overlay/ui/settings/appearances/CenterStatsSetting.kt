package com.voxyl.overlay.ui.settings.appearances

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.voxyl.overlay.business.settings.config.CenterStats
import com.voxyl.overlay.business.settings.config.Config
import com.voxyl.overlay.ui.elements.VCheckbox
import com.voxyl.overlay.ui.elements.VText

@Composable
fun CenterStatsCheckBox(modifier: Modifier = Modifier) {
    var centerStatsHeader by remember { mutableStateOf(Config[CenterStats] != "false") }

    Row(
        modifier = modifier
            .height(44.dp)
            .fillMaxWidth()
            .padding(horizontal = 30.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        VCheckbox(
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

        Spacer(modifier = Modifier.size(16.dp))

        VText("Center the stats in the columns")
    }
}
