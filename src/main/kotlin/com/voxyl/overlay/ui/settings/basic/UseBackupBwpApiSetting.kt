package com.voxyl.overlay.ui.settings.basic

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.voxyl.overlay.controllers.common.ui.MainWhite
import com.voxyl.overlay.ui.elements.VCheckbox
import com.voxyl.overlay.ui.elements.VText

@Composable
fun UseBackupBwpApiCheckbox(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .height(44.dp)
            .fillMaxWidth()
            .padding(horizontal = 30.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        VCheckbox(
            checked = false,
            onCheckedChange = {},
            modifier = Modifier.size(12.dp),
            enabled = false,
        )

        Spacer(modifier = Modifier.size(16.dp))

        VText(
            text = "Use the backup BWP API if you have API key issues (Slower, use own key if it works)",
            alpha = MainWhite.alpha / 2
        )
    }
}
