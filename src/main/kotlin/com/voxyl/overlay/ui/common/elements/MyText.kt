package com.voxyl.overlay.ui.common.elements

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.voxyl.overlay.ui.theme.Fonts

@Composable
fun MyText(
    text: String,
    style: TextStyle = TextStyle(fontFamily = Fonts.nunito()),
    fontSize: TextUnit = 10.sp,
    color: Color = Color.Unspecified,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = style,
        fontSize = fontSize,
        color = color,
        modifier = modifier
    )
}