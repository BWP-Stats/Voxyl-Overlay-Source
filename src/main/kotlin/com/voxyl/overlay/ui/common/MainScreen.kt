@file:Suppress("FunctionName", "ObjectPropertyName")
@file:OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)

package com.voxyl.overlay.ui.common

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.FrameWindowScope
import com.voxyl.overlay.ui.theme.Fonts.nunito
import com.voxyl.overlay.ui.common.util.requestFocusOnClick
import com.voxyl.overlay.ui.main.elements.PlayerStats
import com.voxyl.overlay.ui.main.elements.StatsHeader
import com.voxyl.overlay.ui.settings.elements.Settings

lateinit var window: ComposeWindow

@ExperimentalComposeUiApi
@Composable
fun MainScreen(frameWindowScope: FrameWindowScope) {

    window = frameWindowScope.window

    val lazyListState = rememberLazyListState()
    val settingsMenu = remember { mutableStateOf(false) }
    val statsToShow =
        remember { mutableStateOf(listOf("bwp.level", "name", "bwp.role", "bwp.wins", "bwp.kills", "bwp.finals")) }

    BackgroundBox()
    TitleBox()
    VoxylLogoForTitleBar()

    frameWindowScope.WindowDraggableArea(
        modifier = Modifier.fillMaxWidth().height(64.dp)
    )

    TitleBar(settingsMenu)

    if (settingsMenu.value) {
        Settings()
    } else {
        StatsHeader(statsToShow)
        PlayerStats(statsToShow, lazyListState)
    }
}

@Composable
fun VoxylLogoForTitleBar() {
    Image(
        modifier = Modifier
            .size(60.dp)
            .requestFocusOnClick(),
        painter = painterResource("VoxylLogoForTitleBar.png"),
        contentDescription = null,
        alpha = .8f
    )
}

@Composable
fun BackgroundBox(modifier: Modifier = Modifier, shape: Shape = RoundedCornerShape(10.dp)) = Box(

    modifier = modifier
        .fillMaxSize()
        .clip(shape)
        .background(Color(0f, 0f, 0f, .3f))
        .requestFocusOnClick()
)

@Composable
fun TitleBox(modifier: Modifier = Modifier, shape: Shape = RoundedCornerShape(10.dp)) = Box(

    modifier = modifier
        .absolutePadding(42.dp, 14.dp, 14.dp)
        .fillMaxWidth()
        .height(40.dp)
        .clip(shape)
        .background(Color(0f, 0f, 0f, .2f))
        .requestFocusOnClick()
)

