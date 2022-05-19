package com.voxyl.overlay.ui.mainview.playerstats.colors

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.voxyl.overlay.ui.theme.ErrorColor
import com.voxyl.overlay.ui.theme.am
import kotlin.math.roundToInt

object LevelColors {
    val colorsMap = mapOf(
        -1..-1 to ErrorColor,
        0 until 100 to Color(170, 170, 170),
        100 until 200 to Color.White,
        200 until 300 to Color(255, 170, 0),
        300 until 400 to Color(85, 255, 255),
        400 until 500 to Color(0, 170, 0),
        500 until 600 to Color(0, 170, 170),
        600 until Integer.MAX_VALUE to ErrorColor
    )
    
    fun coloredLevel(level: String): AnnotatedString {
        val levelInt = level.toFloatOrNull()?.floor() ?: -1

        return buildAnnotatedString {
            withStyle(style = SpanStyle(color = colorsMap.entries.first { levelInt in it.key }.value.am)) {
                append("[$level✫]")
            }
        }
    }

    private fun Float.floor(): Int {
        return kotlin.math.floor(this).roundToInt()
    }
}