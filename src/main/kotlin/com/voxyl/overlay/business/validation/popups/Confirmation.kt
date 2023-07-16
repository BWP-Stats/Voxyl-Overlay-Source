package com.voxyl.overlay.business.validation.popups

import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.voxyl.overlay.controllers.common.ui.MainWhite
import com.voxyl.overlay.controllers.common.ui.am

class Confirmation(text: String, duration: Long = 5000L, onConfirmation: () -> Unit) : PopUp(text, duration) {
    override val clickableIcon = Icons.Filled.Check
    override val onClick = onConfirmation

    override val color = Color(107, 214, 110, 200).am
    override val icon: @Composable (Modifier) -> Unit = @Composable {
        Icon(
            painter = painterResource("icons/help-circle.png"),
            contentDescription = "Close",
            tint = MainWhite,
            modifier = it
        )
    }
}
