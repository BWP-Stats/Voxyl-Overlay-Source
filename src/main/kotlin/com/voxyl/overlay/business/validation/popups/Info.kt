package com.voxyl.overlay.business.validation.popups

import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.voxyl.overlay.controllers.common.ui.MainWhite
import com.voxyl.overlay.controllers.common.ui.am

class Info(text: String, duration: Long = 5000L) : PopUp(text, duration) {
    override val color = Color(87, 87, 87, 200).am
    override val icon: @Composable (Modifier) -> Unit = @Composable {
        Icon(
            Icons.Filled.Info,
            contentDescription = "Close",
            tint = MainWhite,
            modifier = it
        )
    }
}
