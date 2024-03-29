package com.voxyl.overlay.ui.entitystats.columns.util

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.voxyl.overlay.business.stats.enitities.Entity
import com.voxyl.overlay.business.utils.ERROR_PLACEHOLDER
import com.voxyl.overlay.controllers.common.ui.am

private val colorsMap = mapOf(
    "owner"        to  mcColors["red"]!!,
    "admin"        to  mcColors["red"]!!,
    "manager"      to  mcColors["dark-red"]!!,
    "srmod"        to  mcColors["yellow"]!!,
    "dev"          to  mcColors["green"]!!,
    "youtube"      to  mcColors["red"]!!,
    "head-builder" to  mcColors["dark-purple"]!!,
    "builder"      to  mcColors["light-purple"]!!,
    "mod"          to  mcColors["yellow"]!!,
    "trainee"      to  mcColors["green"]!!,
    "screenshare"  to  mcColors["blue"]!!,
    "helper"       to  mcColors["aqua"]!!,
    "master"       to  mcColors["gold"]!!,
    "expert"       to  mcColors["blue"]!!,
    "adept"        to  mcColors["dark-green"]!!,
    "none"         to  mcColors["gray"]!!,
    "bot"          to  mcColors["dark-gray"]!!,
    "err"          to  mcColors["red"]!!
)

fun getColorizedBwpRank(player: Entity) = when {
    player.isLoading -> buildAnnotatedString { }

    player["bwp.role"]?.lowercase() == "youtube" -> {
        buildAnnotatedString {
            withStyle(style = SpanStyle(color = colorsMap["youtube"]!!.am)) {
                append("[")
            }
            withStyle(style = SpanStyle(color = Color.White.am)) {
                append(player["bwp.role"] ?: ERROR_PLACEHOLDER)
            }
            withStyle(style = SpanStyle(color = colorsMap["youtube"]!!.am)) {
                append("] ")
            }
        }
    }

    player["bwp.role"]?.lowercase() == "none" -> buildAnnotatedString { }

    else -> {
        buildAnnotatedString {
            withStyle(style = SpanStyle(color = colorsMap[player["bwp.role"]?.lowercase() ?: "err"]!!.am)) {
                append("[${player["bwp.role"] ?: ERROR_PLACEHOLDER}] ")
            }
        }
    }
}

fun getColorizedBwpName(player: Entity): AnnotatedString = when {
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
