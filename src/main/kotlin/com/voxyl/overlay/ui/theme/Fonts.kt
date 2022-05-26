package com.voxyl.overlay.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font
import java.io.File

object Fonts {
    @Composable
    fun nunito() = FontFamily(
        Font(
            "fonts/nunito/Nunito-Black.ttf",
            FontWeight.Black,
            FontStyle.Normal
        ),
        Font(
            "fonts/nunito/Nunito-BlackItalic.ttf",
            FontWeight.Black,
            FontStyle.Italic
        ),
        Font(
            "fonts/nunito/Nunito-Bold.ttf",
            FontWeight.Bold,
            FontStyle.Normal
        ),
        Font(
            "fonts/nunito/Nunito-BoldItalic.ttf",
            FontWeight.Bold,
            FontStyle.Italic
        ),
        Font(
            "fonts/nunito/Nunito-ExtraBold.ttf",
            FontWeight.ExtraBold,
            FontStyle.Normal
        ),
        Font(
            "fonts/nunito/Nunito-ExtraBoldItalic.ttf",
            FontWeight.ExtraBold,
            FontStyle.Italic
        ),
        Font(
            "fonts/nunito/Nunito-ExtraLight.ttf",
            FontWeight.ExtraLight,
            FontStyle.Normal
        ),
        Font(
            "fonts/nunito/Nunito-ExtraLightItalic.ttf",
            FontWeight.ExtraLight,
            FontStyle.Italic
        ),
        Font(
            "fonts/nunito/Nunito-Italic.ttf",
            FontWeight.Normal,
            FontStyle.Italic
        ),
        Font(
            "fonts/nunito/Nunito-Light.ttf",
            FontWeight.Light,
            FontStyle.Normal
        ),
        Font(
            "fonts/nunito/Nunito-LightItalic.ttf",
            FontWeight.Light,
            FontStyle.Italic
        ),
        Font(
            "fonts/nunito/Nunito-Medium.ttf",
            FontWeight.Medium,
            FontStyle.Normal
        ),
        Font(
            "fonts/nunito/Nunito-MediumItalic.ttf",
            FontWeight.Medium,
            FontStyle.Italic
        ),
        Font(
            "fonts/nunito/Nunito-Regular.ttf",
            FontWeight.Normal,
            FontStyle.Normal
        ),
        Font(
            "fonts/nunito/Nunito-SemiBold.ttf",
            FontWeight.SemiBold,
            FontStyle.Normal
        ),
        Font(
            "fonts/nunito/Nunito-SemiBoldItalic.ttf",
            FontWeight.SemiBold,
            FontStyle.Italic
        ),
    )

    private var filePath = "fonts/nunito"

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