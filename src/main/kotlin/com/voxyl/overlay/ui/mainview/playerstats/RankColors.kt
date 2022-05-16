package com.voxyl.overlay.ui.mainview.playerstats

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.voxyl.overlay.data.player.PlayerState
import com.voxyl.overlay.ui.theme.am

object RankColors {

    val colorsMap = mapOf(
        "owner" to Color(255, 85, 85),
        "admin" to Color(255, 85, 85),
        "manager" to Color(170, 0, 0),
        "srmod" to Color(255, 255, 85),
        "dev" to Color(85, 255, 85),
        "youtube" to Color(255, 85, 85),
        "head-builde" to Color(170, 0, 170),
        "builder" to Color(255, 85, 255),
        "mod" to Color(255, 255, 85),
        "trainee" to Color(85, 255, 85),
        "screenshare" to Color(85, 85, 255),
        "helper" to Color(85, 255, 255),
        "master" to Color(255, 170, 0),
        "expert" to Color(85, 85, 255),
        "adept" to Color(0, 170, 0),
        "none" to Color(170, 170, 170),
        "err" to Color(255, 85, 85)
    )


    @Composable
    fun coloredRank(player: PlayerState) = when (player["bwp.role"]?.lowercase() ?: "err") {
        "youtube" -> {
            buildAnnotatedString {
                withStyle(style = SpanStyle(color = colorsMap["youtube"]!!.am)) {
                    append("[")
                }
                withStyle(style = SpanStyle(color = Color.White.am)) {
                    append(player["bwp.role"] ?: "ERR")
                }
                withStyle(style = SpanStyle(color = colorsMap["youtube"]!!.am)) {
                    append("]")
                }
            }
        }

        else -> {
            buildAnnotatedString {
                withStyle(style = SpanStyle(color = colorsMap[player["bwp.role"]?.lowercase() ?: "err"]!!.am)) {
                    append("[${player["bwp.role"] ?: "ERR"}]")
                }
            }
        }
    }

    @Composable
    fun coloredName(player: PlayerState): AnnotatedString = when {

        player.isLoading -> buildAnnotatedString {
            withStyle(style = SpanStyle(color = Color.White.am)) {
                append(player.name)
            }
        }

        player.error.isNotBlank() -> buildAnnotatedString {
            withStyle(style = SpanStyle(color = colorsMap["err"]!!.am)) {
                append("#${player.name}")
            }
        }

        else -> {
            buildAnnotatedString {
                withStyle(style = SpanStyle(color = colorsMap[player["bwp.role"]?.lowercase() ?: "err"]!!.am)) {
                    append(player.name)
                }
            }
        }
    }
}
