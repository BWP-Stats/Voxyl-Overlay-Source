package com.voxyl.overlay.ui.settings.appearances

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.voxyl.overlay.settings.config.Config
import com.voxyl.overlay.settings.config.ConfigKeys.ShowRankPrefix
import com.voxyl.overlay.ui.common.elements.MyCheckbox
import com.voxyl.overlay.ui.theme.VText

@Composable
fun ShowRankPrefixSetting(modifier: Modifier = Modifier) {
    var centerStatsHeader by remember { mutableStateOf(Config[ShowRankPrefix].toBooleanStrictOrNull() ?: false) }

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
                    Config[ShowRankPrefix] = "true"
                    centerStatsHeader = true
                } else {
                    Config[ShowRankPrefix] = "false"
                    centerStatsHeader = false
                }
            },
            modifier = Modifier.size(12.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        VText("Show a player's rank prefix")
    }
}
