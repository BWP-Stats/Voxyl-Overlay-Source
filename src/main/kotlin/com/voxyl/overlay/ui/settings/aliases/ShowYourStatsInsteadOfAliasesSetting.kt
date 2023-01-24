package com.voxyl.overlay.ui.settings.aliases

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.voxyl.overlay.business.settings.config.Config
import com.voxyl.overlay.business.settings.config.PlayerName
import com.voxyl.overlay.business.settings.config.ShowYourStatsInsteadOfAliases
import com.voxyl.overlay.ui.elements.VCheckbox
import com.voxyl.overlay.ui.elements.VText

@Composable
fun ShowYourStatsInsteadOfAliasesCheckBox(modifier: Modifier = Modifier) {
    var showYourStatsInsteadOfAliases by remember { mutableStateOf(Config[ShowYourStatsInsteadOfAliases] == "true") }

    Row(
        modifier = modifier
            .height(44.dp)
            .fillMaxWidth()
            .padding(horizontal = 30.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        VCheckbox(
            checked = showYourStatsInsteadOfAliases,
            onCheckedChange = {
                if (it) {
                    Config[ShowYourStatsInsteadOfAliases] = "true"
                    showYourStatsInsteadOfAliases = true
                } else {
                    Config[ShowYourStatsInsteadOfAliases] = "false"
                    showYourStatsInsteadOfAliases = false
                }
            },
            modifier = Modifier.size(12.dp)
        )

        Spacer(modifier = Modifier.size(16.dp))

        var name = "The one you entered higher up"

        if (Config[PlayerName] != "" && Config[PlayerName].matches(Regex("\\w{1,16}"))) {
            name = Config[PlayerName]
        }

        VText("Show your main account ($name)'s stats instead of the alias's stats")
    }
}
