package com.voxyl.overlay.ui.settings.basic

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.voxyl.overlay.business.settings.config.Config
import com.voxyl.overlay.business.settings.config.ConfigKeys.UseBackupBwpApi
import com.voxyl.overlay.ui.elements.VCheckbox
import com.voxyl.overlay.ui.elements.VText

@Composable
fun UseBackupBwpApiCheckbox(modifier: Modifier = Modifier) {
    var useBackupBwpApi by remember { mutableStateOf(Config[UseBackupBwpApi] != "false") }

    Row(
        modifier = modifier
            .height(44.dp)
            .fillMaxWidth()
            .padding(horizontal = 30.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        VCheckbox(
            checked = useBackupBwpApi,
            onCheckedChange = {
                if (it) {
                    Config[UseBackupBwpApi] = "true"
                    useBackupBwpApi = true
                } else {
                    Config[UseBackupBwpApi] = "false"
                    useBackupBwpApi = false
                }
            },
            modifier = Modifier.size(12.dp)
        )

        Spacer(modifier = Modifier.size(16.dp))

        VText("Use the backup BWP API if you have API key issues (Slower, use own key if it works)")
    }
}
