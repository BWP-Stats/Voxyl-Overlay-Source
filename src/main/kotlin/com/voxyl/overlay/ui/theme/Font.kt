package com.voxyl.overlay.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font
import java.io.File

fun main() {
    Fonts.fontFamilyMaker()
}

object Fonts {
    @Composable
    fun nunito() = FontFamily(
        Font(
            "font/Nunito-Black.ttf",
            FontWeight.Black,
            FontStyle.Normal
        ),
        Font(
            "font/Nunito-BlackItalic.ttf",
            FontWeight.Black,
            FontStyle.Italic
        ),
        Font(
            "font/Nunito-Bold.ttf",
            FontWeight.Bold,
            FontStyle.Normal
        ),
        Font(
            "font/Nunito-BoldItalic.ttf",
            FontWeight.Bold,
            FontStyle.Italic
        ),
        Font(
            "font/Nunito-ExtraBold.ttf",
            FontWeight.ExtraBold,
            FontStyle.Normal
        ),
        Font(
            "font/Nunito-ExtraBoldItalic.ttf",
            FontWeight.ExtraBold,
            FontStyle.Italic
        ),
        Font(
            "font/Nunito-ExtraLight.ttf",
            FontWeight.ExtraLight,
            FontStyle.Normal
        ),
        Font(
            "font/Nunito-ExtraLightItalic.ttf",
            FontWeight.ExtraLight,
            FontStyle.Italic
        ),
        Font(
            "font/Nunito-Italic.ttf",
            FontWeight.Normal,
            FontStyle.Italic
        ),
        Font(
            "font/Nunito-Light.ttf",
            FontWeight.Light,
            FontStyle.Normal
        ),
        Font(
            "font/Nunito-LightItalic.ttf",
            FontWeight.Light,
            FontStyle.Italic
        ),
        Font(
            "font/Nunito-Medium.ttf",
            FontWeight.Medium,
            FontStyle.Normal
        ),
        Font(
            "font/Nunito-MediumItalic.ttf",
            FontWeight.Medium,
            FontStyle.Italic
        ),
        Font(
            "font/Nunito-Regular.ttf",
            FontWeight.Normal,
            FontStyle.Normal
        ),
        Font(
            "font/Nunito-SemiBold.ttf",
            FontWeight.SemiBold,
            FontStyle.Normal
        ),
        Font(
            "font/Nunito-SemiBoldItalic.ttf",
            FontWeight.SemiBold,
            FontStyle.Italic
        ),
    )

    private const val filePath = "font"

    fun fontFamilyMaker() {
        val files: Array<File> = File(this.javaClass.classLoader.getResource(filePath)!!.path).listFiles()!!

        for (i in files.indices) {
            val file = files[i]

            println("""
                Font(
                    "$filePath/${file.name}",
                    FontWeight.${file.name.split("-", ".")[1].replace("Italic", "").replace("Regular", "Normal")},
                    FontStyle.${if (file.name.contains("Italic")) "Italic" else "Normal"}
                ),
            """.trimIndent())
        }
    }
}