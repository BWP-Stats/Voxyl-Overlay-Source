package com.voxyl.overlay.ui.playerstats.playerstats.colors

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.voxyl.overlay.business.player.PlayerState
import com.voxyl.overlay.ui.theme.am

object HypixelRankColors {

    val colors = mapOf(
        "black" to Color(0xFF000000),
        "dark-blue" to Color(0xFF0000AA),
        "dark-green" to Color(0xFF00AA00),
        "dark-aqua" to Color(0xFF00AAAA),
        "dark-red" to Color(0xFFAA0000),
        "dark-purple" to Color(0xFFAA00AA),
        "gold" to Color(0xFFFFAA00),
        "gray" to Color(0xFFAAAAAA),
        "dark-gray" to Color(0xFF555555),
        "blue" to Color(0xFF5555FF),
        "green" to Color(0xFF55FF55),
        "aqua" to Color(0xFF55FFFF),
        "red" to Color(0xFFFF5555),
        "light-purple" to Color(0xFFFF55FF),
        "yellow" to Color(0xFFFFFF55),
        "white" to Color(0xFFFFFFFF)
    )

    val ranks = mapOf(
        "OWNER" to ({ _: PlayerState ->
            buildAnnotatedString {
                withStyle(style = SpanStyle(color = colors["red"]!!.am)) {
                    append("[OWNER]")
                }
            }
        } to colors["red"]!!),

        "YOUTUBER" to ({ _: PlayerState ->
            buildAnnotatedString {
                withStyle(style = SpanStyle(color = colors["red"]!!.am)) {
                    append("[")
                }
                withStyle(style = SpanStyle(color = colors["white"]!!.am)) {
                    append("YOUTUBE")
                }
                withStyle(style = SpanStyle(color = colors["red"]!!.am)) {
                    append("]")
                }
            }
        } to colors["red"]!!),

        "GAME_MASTER" to ({ _: PlayerState ->
            buildAnnotatedString {
                withStyle(style = SpanStyle(color = colors["dark-green"]!!.am)) {
                    append("[GM]")
                }
            }
        } to colors["dark-green"]!!),

        "ADMIN" to ({ _: PlayerState ->
            buildAnnotatedString {
                withStyle(style = SpanStyle(color = colors["red"]!!.am)) {
                    append("[ADMIN]")
                }
            }
        } to colors["red"]!!),

        "PIG+++" to ({ _: PlayerState ->
            buildAnnotatedString {
                withStyle(style = SpanStyle(color = colors["light-purple"]!!.am)) {
                    append("[PIG")
                }
                withStyle(style = SpanStyle(color = colors["aqua"]!!.am)) {
                    append("+++")
                }
                withStyle(style = SpanStyle(color = colors["light-purple"]!!.am)) {
                    append("]")
                }
            }
        } to colors["light-purple"]!!),

        "GMVP++" to ({ ps: PlayerState ->
            buildAnnotatedString {
                withStyle(style = SpanStyle(color = colors["gold"]!!.am)) {
                    append("[MVP")
                }
                withStyle(style = SpanStyle(color = colors[ps["hypixel.rankColor"]]!!.am)) {
                    append("++")
                }
                withStyle(style = SpanStyle(color = colors["gold"]!!.am)) {
                    append("]")
                }
            }
        } to colors["gold"]!!),

        "BMVP++" to ({ ps: PlayerState ->
            buildAnnotatedString {
                withStyle(style = SpanStyle(color = colors["aqua"]!!.am)) {
                    append("[MVP")
                }
                withStyle(style = SpanStyle(color = colors[ps["hypixel.rankColor"]]!!.am)) {
                    append("++")
                }
                withStyle(style = SpanStyle(color = colors["aqua"]!!.am)) {
                    append("]")
                }
            }
        } to colors["aqua"]!!),

        "MVP+" to ({ ps: PlayerState ->
            buildAnnotatedString {
                withStyle(style = SpanStyle(color = colors["aqua"]!!.am)) {
                    append("[MVP")
                }
                withStyle(style = SpanStyle(color = colors[ps["hypixel.rankColor"]]!!.am)) {
                    append("+")
                }
                withStyle(style = SpanStyle(color = colors["aqua"]!!.am)) {
                    append("]")
                }
            }
        } to colors["aqua"]!!),

        "MVP" to ({ _: PlayerState ->
            buildAnnotatedString {
                withStyle(style = SpanStyle(color = colors["aqua"]!!.am)) {
                    append("[MVP]")
                }
            }
        } to colors["aqua"]!!),

        "VIP+" to ({ _: PlayerState ->
            buildAnnotatedString {
                withStyle(style = SpanStyle(color = colors["green"]!!.am)) {
                    append("[VIP]")
                }
                withStyle(style = SpanStyle(color = colors["yellow"]!!.am)) {
                    append("+")
                }
            }
        } to colors["green"]!!),

        "VIP" to ({ _: PlayerState ->
            buildAnnotatedString {
                withStyle(style = SpanStyle(color = colors["green"]!!.am)) {
                    append("[VIP]")
                }
            }
        } to colors["green"]!!),

        "NONE" to ({ _: PlayerState ->
            buildAnnotatedString {
                withStyle(style = SpanStyle(color = colors["gray"]!!.am)) {
                    append("[NONE]")
                }
            }
        } to colors["gray"]!!),

        "ERR" to ({ _: PlayerState ->
            buildAnnotatedString {
                withStyle(style = SpanStyle(color = colors["red"]!!.am)) {
                    append("[ERR]")
                }
            }
        } to colors["red"]!!)
    )


    fun coloredRank(player: PlayerState) = when {
        player.isLoading -> buildAnnotatedString { }
        player.error.isNotBlank() -> ranks["ERR"]!!.first(player) + " ".toAnnotatedString()
        else -> (ranks[player["hypixel.rank"]] ?: ranks["ERR"]!!).first(player)  + " ".toAnnotatedString()
    }

    fun coloredName(player: PlayerState): AnnotatedString = when {
        player.isLoading -> buildAnnotatedString {
            withStyle(style = SpanStyle(color = Color.White.am)) {
                append(player.name)
            }
        }

        player.error.isNotBlank() -> buildAnnotatedString {
            withStyle(style = SpanStyle(color = ranks["ERR"]!!.second.am)) {
                append("#${player.name}")
            }
        }

        else -> buildAnnotatedString {
            withStyle(style = SpanStyle(color = ranks[player["hypixel.rank"] ?: "ERR"]!!.second.am)) {
                append(player.name)
            }
        }
    }

    private fun String.toAnnotatedString(): AnnotatedString {
        return buildAnnotatedString { append(this@toAnnotatedString) }
    }
}
