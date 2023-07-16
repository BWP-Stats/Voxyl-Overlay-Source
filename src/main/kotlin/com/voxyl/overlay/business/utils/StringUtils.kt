package com.voxyl.overlay.business.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.voxyl.overlay.controllers.common.ui.am
import com.voxyl.overlay.ui.entitystats.toAnnotatedString

const val ERROR_PLACEHOLDER = "ERR"

val LOADING_STRING = "...".toAnnotatedString()

val DASH_STRING = "–".toAnnotatedString()

val COLORED_ERROR_PLACEHOLDER = getColoredErrorPlaceholder()

fun getColoredErrorPlaceholder(noHashtag: Boolean = false) = buildAnnotatedString {
    withStyle(style = SpanStyle(color = Color(255, 85, 85).am)) {
        append((if (noHashtag) "" else "#") + ERROR_PLACEHOLDER)
    }
}

fun String.untrimUUID() =
    if (this.length == 32) {
        this.substring(0, 8) + "-" + this.substring(8, 12) + "-" + this.substring(12, 16) + "-" + this.substring(16, 20) + "-" + this.substring(20, 32)
    } else {
        null
    }

