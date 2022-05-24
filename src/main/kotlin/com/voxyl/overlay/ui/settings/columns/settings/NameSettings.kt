package com.voxyl.overlay.ui.settings.columns.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.DropdownMenuItem
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.voxyl.overlay.settings.config.Config
import com.voxyl.overlay.settings.config.ConfigKeys.RankPrefix
import com.voxyl.overlay.settings.config.ConfigKeys.ShowRankPrefix
import com.voxyl.overlay.settings.misc.MiscSettings
import com.voxyl.overlay.ui.elements.MyCheckbox
import com.voxyl.overlay.ui.settings.columns.ColumnSettingsMenuState
import com.voxyl.overlay.ui.theme.VText

@Composable
fun NameSettings() {
    if (ColumnSettingsMenuState.stat != "name") return

    showRankPrefixSetting()
    whichRankPrefixSetting()
}

@Composable
private fun showRankPrefixSetting() {
    var showRankPrefix by remember { mutableStateOf(Config[ShowRankPrefix] != "false") }

    DropdownMenuItem(onClick = {
        showRankPrefix = !showRankPrefix
        Config[ShowRankPrefix] = showRankPrefix.toString()
    }) {
        Row(
            modifier = Modifier
                .height(44.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MyCheckbox(
                checked = showRankPrefix,
                onCheckedChange = {
                    if (it) {
                        Config[ShowRankPrefix] = "true"
                        showRankPrefix = true
                    } else {
                        Config[ShowRankPrefix] = "false"
                        showRankPrefix = false
                    }
                },
                modifier = Modifier.size(12.dp)
            )

            Spacer(modifier = Modifier.size(16.dp))

            VText("Show rank prefix", fontSize = TextUnit.Unspecified)
        }
    }
}

@Composable
private fun whichRankPrefixSetting() {
    var prefix by mutableStateOf(Config[RankPrefix])

    DropdownMenuItem(onClick = {
        prefix = if (prefix == "bwp") "hypixel" else "bwp"
        Config[RankPrefix] = prefix
    }) {
        VText(
            text = if (MiscSettings["first_time_switching_rank"] == "true") {
                MiscSettings["first_time_switching_rank"] = "false"
                "Use $prefix rank (Click me!)"
            } else {
                "Use $prefix rank"
            },
            fontSize = TextUnit.Unspecified,
        )
    }
}