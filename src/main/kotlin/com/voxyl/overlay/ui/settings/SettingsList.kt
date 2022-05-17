@file:OptIn(ExperimentalComposeUiApi::class)

package com.voxyl.overlay.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
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
import com.voxyl.overlay.ui.settings.keybinds.ClearPlayersKeySetting
import com.voxyl.overlay.ui.settings.keybinds.OpenCloseKeySetting
import com.voxyl.overlay.ui.settings.qol.AutoShowAndHideDelaySlider
import com.voxyl.overlay.ui.settings.qol.PinYourselfToTopCheckbox

@Composable
fun Settings(
    modifier: Modifier = Modifier
) {
    val settingsList = rememberLazyListState()
    SettingsList(
        modifier = modifier,
        lazyListState = settingsList
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

    val settings = remember {
        mutableStateListOf<@Composable () -> Unit>(
            { BWPApiKeyTextField() },
            { HypixelApiKeyTextField() },
            { PlayerNameTextField() },
            { LogFilePathTextField() },
            { PinYourselfToTopCheckbox(addYourself) },
            { AddYourselfToOverlayCheckbox(addYourself) },
            { AutoShowAndHideCheckBox(autoHide) },
            { AutoShowAndHideDelaySlider(autoHide) },
            { OpacitySlider() },
            { TitleBarSizeSlider() },
            { CenterStatsCheckBox() },
            { ShowRankPrefixSetting() },
            { RSlider() },
            { GSlider() },
            { BSlider() },
            { OpenCloseKeySetting() },
            { ClearPlayersKeySetting() },
        )
    }

    LazyColumn(
        state = lazyListState,
        modifier = modifier.absoluteOffset(y = 60.tbsm.dp).fillMaxSize().scrollbar(lazyListState).requestFocusOnClick(),
        contentPadding = PaddingValues(bottom = 75.dp)
    ) {
        items(settings) {
            it()
        }
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
