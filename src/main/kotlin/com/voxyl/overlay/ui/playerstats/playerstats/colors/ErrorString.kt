package com.voxyl.overlay.ui.playerstats.playerstats.colors

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.voxyl.overlay.ui.theme.am

object ErrorString {
    fun get(noHashtag: Boolean = false) = buildAnnotatedString {
        withStyle(style = SpanStyle(color = Color(255, 85, 85).am)) {
            append(if (noHashtag) "ERR" else "#ERR")
        }
    }
}