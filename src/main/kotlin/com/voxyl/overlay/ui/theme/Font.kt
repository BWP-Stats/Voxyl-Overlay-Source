package com.voxyl.overlay.ui.theme

import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import java.io.File

@Composable
fun VText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MainWhite,
    fontSize: TextUnit = 12.sp,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = Fonts.nunito(),
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    style: TextStyle = LocalTextStyle.current,
    alpha: Float = 1f
) {
    Text(
        text = text,
        modifier = modifier,
        color = if (alpha != 1f) color.copy(alpha = alpha).am else color,
        fontSize = fontSize,
        fontStyle = fontStyle,
        fontWeight = fontWeight,
        fontFamily = fontFamily,
        letterSpacing = letterSpacing,
        textDecoration = textDecoration,
        textAlign = textAlign,
        lineHeight = lineHeight,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        style = style
    )
}

@Composable
fun VText(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
    color: Color = MainWhite,
    fontSize: TextUnit = 12.sp,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = Fonts.nunito(),
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    style: TextStyle = LocalTextStyle.current,
    alpha: Float = 1f
) {
    Text(
        text = text,
        modifier = modifier,
        color = if (alpha != 1f) color.copy(alpha = alpha).am else color,
        fontSize = fontSize,
        fontStyle = fontStyle,
        fontWeight = fontWeight,
        fontFamily = fontFamily,
        letterSpacing = letterSpacing,
        textDecoration = textDecoration,
        textAlign = textAlign,
        lineHeight = lineHeight,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        style = style
    )
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