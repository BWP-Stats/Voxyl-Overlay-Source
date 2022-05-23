@file:Suppress("FunctionName", "ObjectPropertyName")
@file:OptIn(ExperimentalComposeUiApi::class)

package com.voxyl.overlay.ui.common

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.FrameWindowScope
import com.voxyl.overlay.ui.elements.util.requestFocusOnClick
import com.voxyl.overlay.ui.mainview.playerstats.PlayerStats
import com.voxyl.overlay.kindasortasomewhatviewmodelsishiguessithinkidkwhatevericantbebotheredsmh.ScreenShowing
import com.voxyl.overlay.ui.settings.Settings
import com.voxyl.overlay.ui.theme.bgam
import com.voxyl.overlay.ui.theme.tbsm

@Composable
fun MainScreen(fws: FrameWindowScope) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        BackgroundBox()
        TitleBox()

        TitleBar(fws)

        if (ScreenShowing.screen == "main") {
            PlayerStats()
        } else {
            Settings()
        }

        EventBar()
        AdditionalSettings()
    }
}

@Composable
fun BackgroundBox(modifier: Modifier = Modifier, shape: Shape = RoundedCornerShape(10.dp)) = Box(
    modifier = modifier
        .fillMaxSize()
        .clip(shape)
        .background(Color(.05f, .05f, .05f, .4f).bgam)
        .requestFocusOnClick()
)

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

