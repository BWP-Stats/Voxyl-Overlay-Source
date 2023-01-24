package com.voxyl.overlay.ui.entitystats.colors

import androidx.compose.ui.graphics.Color

val mcColors = mapOf(
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

enum class McColors(val color: Color) {
    Black      (Color(0xFF000000)),
    DarkBlue   (Color(0xFF0000AA)),
    DarkGreen  (Color(0xFF00AA00)),
    DarkAqua   (Color(0xFF00AAAA)),
    DarkRed    (Color(0xFFAA0000)),
    DarkPurple (Color(0xFFAA00AA)),
    Gold       (Color(0xFFFFAA00)),
    Gray       (Color(0xFFAAAAAA)),
    DarkGray   (Color(0xFF555555)),
    Blue       (Color(0xFF5555FF)),
    Green      (Color(0xFF55FF55)),
    Aqua       (Color(0xFF55FFFF)),
    Red        (Color(0xFFFF5555)),
    LightPurple(Color(0xFFFF55FF)),
    Yellow     (Color(0xFFFFFF55)),
    White      (Color(0xFFFFFFFF));

    fun fromKebabCase(color: String): McColors {
        return valueOf(color.kebabToLowerCamelCase())
    }

    private fun String.kebabToLowerCamelCase(): String {
        return "-[a-zA-Z]".toRegex().replace(this) {
            it.value.replace("-","").uppercase()
        }
    }
}
