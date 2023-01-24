package com.voxyl.overlay.controllers.common.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font

object Fonts {
    @Composable
    fun nunito() = FontFamily(
        Font(
            "fonts/Nunito-Regular.ttf",
            FontWeight.Normal,
            FontStyle.Normal
        )
    )
}
