package com.voxyl.overlay.business.validation.popups

import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.voxyl.overlay.controllers.common.ui.MainWhite
import com.voxyl.overlay.controllers.common.ui.am

class Tip(text: String, duration: Long = 5000L) : PopUp(text, duration) {
    override val color = Color(203, 151, 222, 200).am
    override val icon: @Composable (Modifier) -> Unit = @Composable {
        Icon(
            painter = painterResource("icons/lightbulb-multiple-outline.png"),
            contentDescription = "Close",
            tint = MainWhite,
            modifier = it
        )
    }
}
