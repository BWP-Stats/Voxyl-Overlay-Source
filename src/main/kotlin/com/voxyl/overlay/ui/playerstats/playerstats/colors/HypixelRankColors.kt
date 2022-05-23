package com.voxyl.overlay.ui.playerstats.playerstats.colors

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.voxyl.overlay.business.networking.player.PlayerState
import com.voxyl.overlay.ui.theme.am

object HypixelRankColors {
    
    val ranks = mapOf(
        "OWNER" to ({ _: PlayerState ->
            buildAnnotatedString {
                withStyle(style = SpanStyle(color = mcColors["red"]!!.am)) {
                    append("[OWNER]")
                }
            }
        } to mcColors["red"]!!),

        "YOUTUBER" to ({ _: PlayerState ->
            buildAnnotatedString {
                withStyle(style = SpanStyle(color = mcColors["red"]!!.am)) {
                    append("[")
                }
                withStyle(style = SpanStyle(color = mcColors["white"]!!.am)) {
                    append("YOUTUBE")
                }
                withStyle(style = SpanStyle(color = mcColors["red"]!!.am)) {
                    append("]")
                }
            }
        } to mcColors["red"]!!),

        "GAME_MASTER" to ({ _: PlayerState ->
            buildAnnotatedString {
                withStyle(style = SpanStyle(color = mcColors["dark-green"]!!.am)) {
                    append("[GM]")
                }
            }
        } to mcColors["dark-green"]!!),

        "ADMIN" to ({ _: PlayerState ->
            buildAnnotatedString {
                withStyle(style = SpanStyle(color = mcColors["red"]!!.am)) {
                    append("[ADMIN]")
                }
            }
        } to mcColors["red"]!!),

        "PIG+++" to ({ _: PlayerState ->
            buildAnnotatedString {
                withStyle(style = SpanStyle(color = mcColors["light-purple"]!!.am)) {
                    append("[PIG")
                }
                withStyle(style = SpanStyle(color = mcColors["aqua"]!!.am)) {
                    append("+++")
                }
                withStyle(style = SpanStyle(color = mcColors["light-purple"]!!.am)) {
                    append("]")
                }
            }
        } to mcColors["light-purple"]!!),

        "GMVP++" to ({ ps: PlayerState ->
            buildAnnotatedString {
                withStyle(style = SpanStyle(color = mcColors["gold"]!!.am)) {
                    append("[MVP")
                }
                withStyle(style = SpanStyle(color = mcColors[ps["hypixel.rankColor"]]!!.am)) {
                    append("++")
                }
                withStyle(style = SpanStyle(color = mcColors["gold"]!!.am)) {
                    append("]")
                }
            }
        } to mcColors["gold"]!!),

        "BMVP++" to ({ ps: PlayerState ->
            buildAnnotatedString {
                withStyle(style = SpanStyle(color = mcColors["aqua"]!!.am)) {
                    append("[MVP")
                }
                withStyle(style = SpanStyle(color = mcColors[ps["hypixel.rankColor"]]!!.am)) {
                    append("++")
                }
                withStyle(style = SpanStyle(color = mcColors["aqua"]!!.am)) {
                    append("]")
                }
            }
        } to mcColors["aqua"]!!),

        "MVP+" to ({ ps: PlayerState ->
            buildAnnotatedString {
                withStyle(style = SpanStyle(color = mcColors["aqua"]!!.am)) {
                    append("[MVP")
                }
                withStyle(style = SpanStyle(color = mcColors[ps["hypixel.rankColor"]]!!.am)) {
                    append("+")
                }
                withStyle(style = SpanStyle(color = mcColors["aqua"]!!.am)) {
                    append("]")
                }
            }
        } to mcColors["aqua"]!!),

        "MVP" to ({ _: PlayerState ->
            buildAnnotatedString {
                withStyle(style = SpanStyle(color = mcColors["aqua"]!!.am)) {
                    append("[MVP]")
                }
            }
        } to mcColors["aqua"]!!),

        "VIP+" to ({ _: PlayerState ->
            buildAnnotatedString {
                withStyle(style = SpanStyle(color = mcColors["green"]!!.am)) {
                    append("[VIP]")
                }
                withStyle(style = SpanStyle(color = mcColors["yellow"]!!.am)) {
                    append("+")
                }
            }
        } to mcColors["green"]!!),

        "VIP" to ({ _: PlayerState ->
            buildAnnotatedString {
                withStyle(style = SpanStyle(color = mcColors["green"]!!.am)) {
                    append("[VIP]")
                }
            }
        } to mcColors["green"]!!),

        "NONE" to ({ _: PlayerState ->
            buildAnnotatedString {
                withStyle(style = SpanStyle(color = mcColors["gray"]!!.am)) {
                    append("[NONE]")
                }
            }
        } to mcColors["gray"]!!),

        "ERR" to ({ _: PlayerState ->
            buildAnnotatedString {
                withStyle(style = SpanStyle(color = mcColors["red"]!!.am)) {
                    append("[ERR]")
                }
            }
        } to mcColors["red"]!!)
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
