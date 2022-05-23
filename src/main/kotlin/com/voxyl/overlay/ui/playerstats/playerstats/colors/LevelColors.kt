package com.voxyl.overlay.ui.playerstats.playerstats.colors

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.voxyl.overlay.ui.theme.am
import io.github.aakira.napier.Napier

object LevelColors {

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

    //  \.\.\.new Array\((\d)\)\.fill\(\\(".{0,16})\\"\)
    //  " + "\\$2\\", ".repeat($1).trim(',', ' ') + "
    val prestiges: JsonArray by lazy {
        Gson().fromJson(
            "[{\"level\":0,\"star\":\"✫\",\"color\":\"gray\"}," +
                    "{\"level\":100,\"star\":\"✫\",\"color\":\"white\"}," +
                    "{\"level\":200,\"star\":\"✫\",\"color\":\"gold\"}," +
                    "{\"level\":300,\"star\":\"✫\",\"color\":\"aqua\"}," +
                    "{\"level\":400,\"star\":\"✫\",\"color\":\"dark-green\"}," +
                    "{\"level\":500,\"star\":\"✫\",\"color\":\"dark-aqua\"}," +
                    "{\"level\":600,\"star\":\"✫\",\"color\":\"dark-red\"}," +
                    "{\"level\":700,\"star\":\"✫\",\"color\":\"light-purple\"}," +
                    "{\"level\":800,\"star\":\"✫\",\"color\":\"blue\"}," +
                    "{\"level\":900,\"star\":\"✫\",\"color\":\"dark-purple\"}," +
                    "{\"level\":1000,\"star\":\"✫\",\"colors\":[\"red\",\"gold\",\"yellow\",\"green\",\"aqua\",\"light-purple\",\"dark-purple\"]}," +
                    "{\"level\":1100,\"star\":\"✪\",\"colors\":[\"gray\",\"white\",\"white\",\"white\",\"white\",\"gray\",\"gray\"]}," +
                    "{\"level\":1200,\"star\":\"✪\",\"colors\":[\"gray\",\"yellow\",\"yellow\",\"yellow\",\"yellow\",\"gold\",\"gray\"]}," +
                    "{\"level\":1300,\"star\":\"✪\",\"colors\":[\"gray\",\"aqua\",\"aqua\",\"aqua\",\"aqua\",\"dark-aqua\",\"gray\"]}," +
                    "{\"level\":1400,\"star\":\"✪\",\"colors\":[\"gray\",\"green\",\"green\",\"green\",\"green\",\"dark-green\",\"gray\"]}," +
                    "{\"level\":1500,\"star\":\"✪\",\"colors\":[\"gray\",\"dark-aqua\",\"dark-aqua\",\"dark-aqua\",\"dark-aqua\",\"blue\",\"gray\"]}," +
                    "{\"level\":1600,\"star\":\"✪\",\"colors\":[\"gray\",\"red\",\"red\",\"red\",\"red\",\"dark-red\",\"gray\"]}," +
                    "{\"level\":1700,\"star\":\"✪\",\"colors\":[\"gray\",\"light-purple\",\"light-purple\",\"light-purple\",\"light-purple\",\"dark-purple\",\"gray\"]}," +
                    "{\"level\":1800,\"star\":\"✪\",\"colors\":[\"gray\",\"blue\",\"blue\",\"blue\",\"blue\",\"dark-blue\",\"gray\"]}," +
                    "{\"level\":1900,\"star\":\"✪\",\"colors\":[\"gray\",\"dark-purple\",\"dark-purple\",\"dark-purple\",\"dark-purple\",\"dark-gray\",\"gray\"]}," +
                    "{\"level\":2000,\"star\":\"✪\",\"colors\":[\"dark-gray\",\"gray\",\"white\",\"white\",\"gray\",\"gray\",\"dark-gray\"]}," +
                    "{\"level\":2100,\"star\":\"⚝\",\"colors\":[\"white\",\"white\",\"yellow\",\"yellow\",\"gold\",\"gold\",\"gold\"]}," +
                    "{\"level\":2200,\"star\":\"⚝\",\"colors\":[\"gold\",\"gold\",\"white\",\"white\",\"aqua\",\"dark-aqua\",\"dark-aqua\"]}," +
                    "{\"level\":2300,\"star\":\"⚝\",\"colors\":[\"dark-purple\",\"dark-purple\",\"light-purple\",\"light-purple\",\"gold\",\"yellow\",\"yellow\"]}," +
                    "{\"level\":2400,\"star\":\"⚝\",\"colors\":[\"aqua\",\"aqua\",\"white\",\"white\",\"gray\",\"gray\",\"dark-gray\"]}," +
                    "{\"level\":2500,\"star\":\"⚝\",\"colors\":[\"white\",\"white\",\"green\",\"green\",\"dark-green\",\"dark-green\",\"dark-green\"]}," +
                    "{\"level\":2600,\"star\":\"⚝\",\"colors\":[\"dark-red\",\"dark-red\",\"red\",\"red\",\"light-purple\",\"light-purple\",\"dark-purple\"]}," +
                    "{\"level\":2700,\"star\":\"⚝\",\"colors\":[\"dark-red\",\"dark-red\",\"red\",\"red\",\"light-purple\",\"light-purple\",\"dark-purple\"]}," +
                    "{\"level\":2800,\"star\":\"⚝\",\"colors\":[\"green\",\"green\",\"dark-green\",\"dark-green\",\"gold\",\"gold\",\"yellow\"]}," +
                    "{\"level\":2900,\"star\":\"⚝\",\"colors\":[\"aqua\",\"aqua\",\"dark-aqua\",\"dark-aqua\",\"blue\",\"blue\",\"dark-blue\"]}," +
                    "{\"level\":3000,\"star\":\"⚝\",\"colors\":[\"yellow\",\"yellow\",\"gold\",\"gold\",\"red\",\"red\",\"dark-red\"]}]",
            JsonArray::class.java
        )
    }

    fun coloredLevel(level: String): AnnotatedString {
        val prestige = (level.toIntOrNull() ?: 0).div(100).coerceIn(0..30)
        val json = prestiges[prestige].asJsonObject

        return buildAnnotatedString {
            try {
                when {
                    json.has("color") -> {
                        val color = json.get("color").asString
                        withStyle(style = SpanStyle(color = colors[color]!!.am)) {
                            append("[$level${json.get("star").asString}]")
                        }
                    }
                    else -> {
                        val presColors = json.get("colors").asJsonArray
                        val parts = "[$level${json.get("star").asString}]".split("").filter { it != "" }

                        for (i in parts.indices) {
                            val color = presColors[i].asString
                            withStyle(style = SpanStyle(color = colors[color]!!.am)) {
                                append(parts[i])
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                Napier.wtf(e) { "Failed to color level $level" }
                append("[$level✫]")
            }
        }
    }
}