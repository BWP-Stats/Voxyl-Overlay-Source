@file:Suppress("FunctionName", "ObjectPropertyName")

package com.voxyl.overlay.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.FrameWindowScope
import com.voxyl.overlay.controllers.common.ScreenShowing
import com.voxyl.overlay.ui.elements.util.requestFocusOnClick
import com.voxyl.overlay.controllers.common.ui.bgam
import com.voxyl.overlay.controllers.common.ui.tbsm

@Composable
fun FrameWindowScope.MainScreen() {
    Box(
        modifier = Modifier.fillMaxSize()
            .clip(RoundedCornerShape(10.dp))
            .background(Color(.05f, .05f, .05f, .4f).bgam)
            .requestFocusOnClick()
    ) {
        TitleBox()
        TitleBar()

        ScreenShowing.screen()

        PopUpBar()
        AdditionalSettings()
    }
}

@Composable
fun TitleBox(modifier: Modifier = Modifier, shape: Shape = RoundedCornerShape(10.dp)) = Box(
    modifier = modifier
        .absolutePadding(42.tbsm.dp, 14.tbsm.dp, 14.tbsm.dp)
        .fillMaxWidth()
        .height(40.tbsm.dp)
        .clip(shape)
        .background(Color(.15f, .15f, .15f, .4f).bgam)
        .requestFocusOnClick()
)

