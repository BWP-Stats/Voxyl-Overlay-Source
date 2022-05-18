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
import com.voxyl.overlay.ui.common.elements.*
import com.voxyl.overlay.ui.common.util.requestFocusOnClick
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
import com.voxyl.overlay.ui.settings.qol.AutoShowAndHideDelaySlider
import com.voxyl.overlay.ui.settings.qol.PinYourselfToTopCheckbox

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
        mutableStateOf(Config[AddYourselfToOverlay].toBooleanStrictOrNull() ?: true)
    }

    val autoHide = remember {
        mutableStateOf(Config[AutoShowAndHide].toBooleanStrictOrNull() ?: true)
    }

    val settings = mutableListOf(
        @Composable { header("Basic") } to "Basic BWPApiKeyTextField HypixelApiKeyTextField PlayerNameTextField Username LogFilePathTextField",
        @Composable { BWPApiKeyTextField() } to "Basic BWPApiKeyTextField",
        @Composable { HypixelApiKeyTextField() } to "Basic HypixelApiKeyTextField",
        @Composable { PlayerNameTextField() } to "Basic PlayerNameTextField Username",
        @Composable { LogFilePathTextField(); Spacer(Modifier.height(10.dp)) } to "Basic LogFilePathTextField",

        @Composable { header("QOL") } to "QOL PinYourselfToTopCheckbox AddYourselfToOverlayCheckbox AutoShowAndHideCheckBox AutoShowAndHideDelaySlider AutoHide",
        @Composable { PinYourselfToTopCheckbox(addYourself) } to "QOL PinYourselfToTopCheckbox",
        @Composable { AddYourselfToOverlayCheckbox(addYourself) } to "QOL AddYourselfToOverlayCheckbox",
        @Composable { AutoShowAndHideCheckBox(autoHide) } to "QOL AutoShowAndHideCheckBox AutoHide",
        @Composable { AutoShowAndHideDelaySlider(autoHide) } to "QOL AutoShowAndHideDelaySlider AutoHide",

        @Composable { header("Appearance") } to "OpacitySlider TitleBarSizeSlider CenterStateCheckBox ShowRankPrefixSetting RSlider GSlider BSlider Red Green Blue",
        @Composable { BackgroundOpacitySlider() } to "Appearance BackgroundOpacitySlider",
        @Composable { OpacitySlider() } to "Appearance OpacitySlider",
        @Composable { TitleBarSizeSlider() } to "Appearance TitleBarSizeSlider",
        @Composable { CenterStatsCheckBox() } to "Appearance CenterStateCheckBox",
        @Composable { ShowRankPrefixSetting() } to "Appearance ShowRankPrefixSetting",
        @Composable { RSlider() } to "Appearance RSlider Red",
        @Composable { GSlider() } to "Appearance GSlider Green",
        @Composable { BSlider() } to "Appearance BSlider Blue",

        @Composable { header("Keybinds") } to "Keybinds OpenCloseKeySetting ClearPlayersKeySetting Hotkeys",
        @Composable { OpenCloseKeySetting() } to "Keybinds OpenCloseKeySetting Hotkeys",
        @Composable { ClearPlayersKeySetting() } to "Keybinds ClearPlayersKeySetting Hotkeys",

        @Composable { header("Columns") } to "Columns Stats Show",
        @Composable { ColumnsSettings() } to "Columns Stats Show"
    ).filter {
        it.second.contains(queriedSetting.text, true)
    }

    LazyColumn(
        state = lazyListState,
        modifier = modifier
            .absoluteOffset(y = 60.tbsm.dp)
            .fillMaxSize()
            .scrollbar(lazyListState)
            .requestFocusOnClick(),
        contentPadding = PaddingValues(bottom = 75.dp)
    ) {
        items(settings) {
            it.first()
        }
    }
}

@Composable
private fun header(text: String) {
    Row(
        Modifier.fillMaxWidth().height(30.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Divider(modifier = Modifier.width(100.dp).absolutePadding(left = 20.dp), color = MainWhite)
        VText(text, modifier = Modifier.padding(5.dp))
        Divider(modifier = Modifier.weight(1f).absolutePadding(right = 20.dp), color = MainWhite)
    }
}

@Composable
fun SettingsTextField(
    text: String,
    value: TextFieldValue,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    onValueChange: (TextFieldValue) -> Unit,
    doOnEnter: () -> Unit,
    isValid: (TextFieldValue) -> Boolean = { true },
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    val focusManager = LocalFocusManager.current

    MyTextField(
        value = value,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .height(50.dp)
            .onEnterOrEsc(
                focusManager,
                doOnEnter,
                value,
                isValid
            ),
        label = {
            VText(
                text = text,
                modifier = Modifier.absoluteOffset(y = 6.dp),
                fontSize = 10.sp
            )
        },
        placeholder = {
            VText(placeholder, alpha = .313f)
        },
        onValueChange = {
            onValueChange(it)
        },
        trailingIcon = trailingIcon ?: {
            MyTrailingIcon(
                modifier = Modifier
                    .offset(x = 10.dp, y = 5.dp)
                    .size(12.dp, 12.dp)
            ) {
                if (isValid(value)) doOnEnter()
            }
        },
        isError = !isValid(value)
    )
}
