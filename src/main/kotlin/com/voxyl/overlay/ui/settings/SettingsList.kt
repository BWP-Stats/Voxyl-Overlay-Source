@file:OptIn(ExperimentalComposeUiApi::class)

package com.voxyl.overlay.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.Divider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.voxyl.overlay.settings.config.Config
import com.voxyl.overlay.settings.config.ConfigKeys.AddYourselfToOverlay
import com.voxyl.overlay.settings.config.ConfigKeys.AutoShowAndHide
import com.voxyl.overlay.ui.elements.*
import com.voxyl.overlay.ui.elements.util.requestFocusOnClick
import com.voxyl.overlay.ui.settings.appearances.*
import com.voxyl.overlay.ui.settings.basic.BWPApiKeyTextField
import com.voxyl.overlay.ui.settings.basic.HypixelApiKeyTextField
import com.voxyl.overlay.ui.settings.basic.LogFilePathTextField
import com.voxyl.overlay.ui.settings.qol.AutoShowAndHideCheckBox
import com.voxyl.overlay.ui.settings.qol.AddYourselfToOverlayCheckbox
import com.voxyl.overlay.ui.theme.*
import com.voxyl.overlay.ui.settings.basic.PlayerNameTextField
import com.voxyl.overlay.ui.settings.columns.ColumnsSettings
import com.voxyl.overlay.ui.settings.keybinds.ClearPlayersKeySetting
import com.voxyl.overlay.ui.settings.keybinds.OpenCloseKeySetting
import com.voxyl.overlay.ui.settings.keybinds.RefreshPlayersKeySetting
import com.voxyl.overlay.ui.settings.qol.AutoShowAndHideDelaySlider
import com.voxyl.overlay.ui.settings.qol.PinYourselfToTopCheckbox
import io.github.aakira.napier.Napier

@Composable
fun Settings(
    modifier: Modifier = Modifier
) {
    SettingsList(
        modifier = modifier,
        lazyListState = rememberLazyListState()
    )
}

@Composable
fun SettingsList(
    lazyListState: LazyListState,
    modifier: Modifier = Modifier
) {
    val addYourself = remember {
        mutableStateOf(Config[AddYourselfToOverlay] != "false")
    }

    val autoHide = remember {
        mutableStateOf(Config[AutoShowAndHide] != "false")
    }

    val rawSettings by remember {
        mutableStateOf(
            listOf(
                @Composable { Header("Basic") } to "BasicBWPApiKeyTextFieldHypixelApiKeyTextFieldPlayerNameTextFieldUsernameLogFilePathTextField",
                @Composable { BWPApiKeyTextField() } to "BasicBWPApiKeyTextField",
                @Composable { HypixelApiKeyTextField() } to "BasicHypixelApiKeyTextField",
                @Composable { PlayerNameTextField() } to "BasicPlayerNameTextField Username",
                @Composable { LogFilePathTextField() } to "BasicLogFilePathTextField",
                @Composable { Spacer(modifier = Modifier.size(10.dp)) } to "NeverGonnaGiveYouUpNeverGonnaLetYouDown",

                @Composable { Header("QOL") } to "QOLPinYourselfToTopCheckboxAddYourselfToOverlayCheckboxAutoShowAndHideCheckBoxAutoShowAndHideDelaySliderAutoHide",
                @Composable { PinYourselfToTopCheckbox(addYourself) } to "QOLPinYourselfToTopCheckbox",
                @Composable { AddYourselfToOverlayCheckbox(addYourself) } to "QOLAddYourselfToOverlayCheckbox",
                @Composable { AutoShowAndHideCheckBox(autoHide) } to "QOLAutoShowAndHideCheckBoxAutoHide",
                @Composable { AutoShowAndHideDelaySlider(autoHide) } to "QOLAutoShowAndHideDelaySliderAutoHide",

                @Composable { Header("Appearance") } to "AppearanceOpacitySliderTitleBarSizeSliderCenterStatsCheckBoxRSliderGSliderBSliderRedGreenBlue",
                @Composable { BackgroundOpacitySlider() } to "AppearanceBackgroundOpacitySlider",
                @Composable { OpacitySlider() } to "AppearanceOpacitySlider",
                @Composable { TitleBarSizeSlider() } to "AppearanceTitleBarSizeSlider",
                @Composable { CenterStatsCheckBox() } to "AppearanceCenterStatsCheckBox",
                @Composable { RSlider() } to "AppearanceRSliderRed",
                @Composable { GSlider() } to "AppearanceGSliderGreen",
                @Composable { BSlider() } to "AppearanceBSliderBlue",

                @Composable { Header("Keybinds") } to "KeybindsOpenCloseKeySettingClearPlayersKeySettingHotkeysRefreshKeybinds",
                @Composable { OpenCloseKeySetting() } to "KeybindsOpenCloseKeySettingHotkeys",
                @Composable { ClearPlayersKeySetting() } to "KeybindsClearPlayersKeySettingHotkeys",
                @Composable { RefreshPlayersKeySetting() } to "KeybindsRefreshPlayersKeySettingHotkeys",

                @Composable { Header("Columns") } to "ColumnsStatsShowRows",
                @Composable { ColumnsSettings() } to "ColumnsStatsShowRows"
            )
        )
    }

    val filteredSettings = if (queriedSetting.text.isEmpty()) {
        rawSettings
    } else {
        rawSettings.filter {
            it.second.contains(queriedSetting.text.replace(" ", ""), true)
        }
    }

    LazyColumn(
        state = lazyListState,
        modifier = modifier
            .absoluteOffset(y = 60.tbsm.dp)
            .scrollbar(lazyListState)
            .fillMaxSize()
            .requestFocusOnClick(),
        contentPadding = PaddingValues(bottom = 70.dp)
    ) {
        items(filteredSettings) {
            it.first()
        }
    }
}

@Composable
private fun Header(text: String) {
    Row(
        Modifier.fillMaxWidth().height(30.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Divider(modifier = Modifier.width(100.dp).absolutePadding(left = 20.dp), color = MainWhite)
        VText(text, modifier = Modifier.padding(5.dp))
        Divider(modifier = Modifier.weight(1f).absolutePadding(right = 20.dp), color = MainWhite)
    }
}