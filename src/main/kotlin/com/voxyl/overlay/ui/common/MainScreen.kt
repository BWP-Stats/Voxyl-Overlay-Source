@file:Suppress("FunctionName", "ObjectPropertyName")
@file:OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)

package com.voxyl.overlay.ui.common

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.FrameWindowScope
import com.voxyl.overlay.ui.common.util.requestFocusOnClick
import com.voxyl.overlay.ui.mainview.playerstats.PlayerStats
import com.voxyl.overlay.ui.mainview.playerstats.StatsHeader
import com.voxyl.overlay.ui.settings.Settings
import com.voxyl.overlay.ui.theme.MainColor
import com.voxyl.overlay.ui.theme.amf
import com.voxyl.overlay.ui.theme.tbsm

@ExperimentalComposeUiApi
@Composable
fun MainScreen(fws: FrameWindowScope) {

    val playerStatsLazyListState = rememberLazyListState()
    val settingsMenu = remember { mutableStateOf(false) }
    val additionalSettingsEnabled = remember { mutableStateOf(false) }
    val statsToShow = remember { mutableStateListOf("bwp.level", "name", "bwp.wins", "bwp.kills", "bwp.finals") }

    BackgroundBox()
    TitleBox()

    VoxylLogoForTitleBar()

    fws.WindowDraggableArea(modifier = Modifier.fillMaxWidth().height(64.dp).requestFocusOnClick())

    TitleBarButtonsAndFields(settingsMenu, additionalSettingsEnabled)

    if (settingsMenu.value) {
        Settings()
    } else {
        StatsHeader(statsToShow)
        PlayerStats(statsToShow, playerStatsLazyListState)
    }

    AdditionalSettings(additionalSettingsEnabled)
}

@Composable
fun VoxylLogoForTitleBar() {
    Image(
        modifier = Modifier
            .size(60.tbsm.dp)
            .requestFocusOnClick(),
        painter = painterResource("VoxylLogoForTitleBar.png"),
        contentDescription = null,
        alpha = .8f.amf,
        colorFilter = ColorFilter.tint(MainColor.value.copy(alpha = 1f), BlendMode.SrcIn)
    )
}

@Composable
fun BackgroundBox(modifier: Modifier = Modifier, shape: Shape = RoundedCornerShape(10.dp)) = Box(
    modifier = modifier
        .fillMaxSize()
        .clip(shape)
        .background(Color(0f, 0f, 0f, .4f))
        .requestFocusOnClick()
)

@Composable
fun TitleBox(modifier: Modifier = Modifier, shape: Shape = RoundedCornerShape(10.dp)) = Box(
    modifier = modifier
        .absolutePadding(42.tbsm.dp, 14.tbsm.dp, 14.tbsm.dp)
        .fillMaxWidth()
        .height(40.tbsm.dp)
        .clip(shape)
        .background(Color(0f, 0f, 0f, .2f))
        .requestFocusOnClick()
)

