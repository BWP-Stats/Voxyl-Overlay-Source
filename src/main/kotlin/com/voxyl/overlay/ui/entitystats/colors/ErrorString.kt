package com.voxyl.overlay.ui.entitystats.colors

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.voxyl.overlay.controllers.common.ui.am

const val ERROR_PLACEHOLDER = "ERR"

fun getColoredErrorPlaceholder(noHashtag: Boolean = false) = buildAnnotatedString {
    withStyle(style = SpanStyle(color = Color(255, 85, 85).am)) {
        append((if (noHashtag) "" else "#") + ERROR_PLACEHOLDER)
    }
}
