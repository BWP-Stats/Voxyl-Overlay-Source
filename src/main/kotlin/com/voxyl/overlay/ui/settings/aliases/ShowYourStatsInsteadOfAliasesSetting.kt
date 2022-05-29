package com.voxyl.overlay.ui.settings.aliases

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.voxyl.overlay.business.settings.config.Config
import com.voxyl.overlay.ui.elements.VCheckbox
import com.voxyl.overlay.ui.elements.VText

@Composable
fun ShowYourStatsInsteadOfAliasesCheckBox(modifier: Modifier = Modifier) {
    var showYourStatsInsteadOfAliases by remember { mutableStateOf(Config["show_your_stats_instead_of_aliases"] == "true") }

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
                    Config["show_your_stats_instead_of_aliases"] = "true"
                    showYourStatsInsteadOfAliases = true
                } else {
                    Config["show_your_stats_instead_of_aliases"] = "false"
                    showYourStatsInsteadOfAliases = false
                }
            },
            modifier = Modifier.size(12.dp)
        )

        Spacer(modifier = Modifier.size(16.dp))

        var name = "The one you entered higher up"

        if (Config["player_name"] != null && Config["player_name"] != "" && Config["player_name"]!!.matches(Regex("\\w{1,16}"))) {
            name = Config["player_name"]!!
        }

        VText("Show your main account ($name)'s stats instead of the alias's stats")
    }
}
