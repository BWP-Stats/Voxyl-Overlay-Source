package com.voxyl.overlay.ui.entitystats.stats.util

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.voxyl.overlay.business.statsfetching.enitities.Entity
import com.voxyl.overlay.controllers.common.ui.am
import com.voxyl.overlay.ui.entitystats.colors.ERROR_PLACEHOLDER
import com.voxyl.overlay.ui.entitystats.colors.mcColors

private val ranks = mapOf(
    "OWNER" to ({ _: Entity ->
        buildAnnotatedString {
            withStyle(style = SpanStyle(color = mcColors["red"]!!.am)) {
                append("[OWNER]")
            }
        }
    } to mcColors["red"]!!),

    "YOUTUBER" to ({ _: Entity ->
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

    "GAME_MASTER" to ({ _: Entity ->
        buildAnnotatedString {
            withStyle(style = SpanStyle(color = mcColors["dark-green"]!!.am)) {
                append("[GM]")
            }
        }
    } to mcColors["dark-green"]!!),

    "ADMIN" to ({ _: Entity ->
        buildAnnotatedString {
            withStyle(style = SpanStyle(color = mcColors["red"]!!.am)) {
                append("[ADMIN]")
            }
        }
    } to mcColors["red"]!!),

    "PIG+++" to ({ _: Entity ->
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

    "GMVP++" to ({ ps: Entity ->
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

    "BMVP++" to ({ ps: Entity ->
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

    "MVP+" to ({ ps: Entity ->
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

    "MVP" to ({ _: Entity ->
        buildAnnotatedString {
            withStyle(style = SpanStyle(color = mcColors["aqua"]!!.am)) {
                append("[MVP]")
            }
        }
    } to mcColors["aqua"]!!),

    "VIP+" to ({ _: Entity ->
        buildAnnotatedString {
            withStyle(style = SpanStyle(color = mcColors["green"]!!.am)) {
                append("[VIP]")
            }
            withStyle(style = SpanStyle(color = mcColors["yellow"]!!.am)) {
                append("+")
            }
        }
    } to mcColors["green"]!!),

    "VIP" to ({ _: Entity ->
        buildAnnotatedString {
            withStyle(style = SpanStyle(color = mcColors["green"]!!.am)) {
                append("[VIP]")
            }
        }
    } to mcColors["green"]!!),

    "BOT" to ({ _: Entity ->
        buildAnnotatedString {
            withStyle(style = SpanStyle(color = mcColors["dark-gray"]!!.am)) {
                append("[BOT]")
            }
        }
    } to mcColors["dark-gray"]!!),

    "NONE" to ({ _: Entity ->
        buildAnnotatedString {
            withStyle(style = SpanStyle(color = mcColors["gray"]!!.am)) {
                append("[NONE]")
            }
        }
    } to mcColors["gray"]!!),

    ERROR_PLACEHOLDER to ({ _: Entity ->
        buildAnnotatedString {
            withStyle(style = SpanStyle(color = mcColors["red"]!!.am)) {
                append("[$ERROR_PLACEHOLDER]")
            }
        }
    } to mcColors["red"]!!)
)

fun getColorizedHypixelRank(player: Entity) = when {
    player.isLoading -> buildAnnotatedString { }
    player.error.isNotBlank() -> ranks[ERROR_PLACEHOLDER]!!.first(player) + " ".toAnnotatedString()
    else -> (ranks[player["hypixel.rank"]] ?: ranks[ERROR_PLACEHOLDER]!!).first(player) + " ".toAnnotatedString()
}

fun getColorizedHypixelName(player: Entity): AnnotatedString = when {
    player.isLoading -> buildAnnotatedString {
        withStyle(style = SpanStyle(color = Color.White.am)) {
            append(player.name)
        }
    }

    player.error.isNotBlank() -> buildAnnotatedString {
        withStyle(style = SpanStyle(color = ranks[ERROR_PLACEHOLDER]!!.second.am)) {
            append("#${player.name}")
        }
    }

    else -> buildAnnotatedString {
        withStyle(style = SpanStyle(color = ranks[player["hypixel.rank"] ?: ERROR_PLACEHOLDER]!!.second.am)) {
            append(player.name)
        }
    }
}

private fun String.toAnnotatedString(): AnnotatedString {
    return buildAnnotatedString { append(this@toAnnotatedString) }
}
