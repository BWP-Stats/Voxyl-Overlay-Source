@file:OptIn(ExperimentalComposeUiApi::class)

package com.voxyl.overlay.ui.settings.elements

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.voxyl.overlay.config.Config
import com.voxyl.overlay.config.Config.Keys.*
import com.voxyl.overlay.ui.common.elements.MyTextField
import com.voxyl.overlay.ui.common.elements.MyTrailingIcon
import com.voxyl.overlay.ui.common.elements.onEnterOrEsc
import com.voxyl.overlay.ui.common.util.requestFocusOnClick
import com.voxyl.overlay.ui.main.elements.scrollbar
import com.voxyl.overlay.ui.settings.elements.basic.BWPApiKeyTextField
import com.voxyl.overlay.ui.settings.elements.basic.HypixelApiKeyTextField
import com.voxyl.overlay.ui.settings.elements.basic.LogFilePathTextField
import com.voxyl.overlay.ui.settings.elements.qol.AutoShowAndHideCheckBox
import com.voxyl.overlay.ui.settings.elements.sources.AddYourselfToOverlayCheckbox
import com.voxyl.overlay.ui.theme.*
import com.voxyl.overlay.ui.common.elements.MyText
import com.voxyl.overlay.ui.settings.elements.basic.PlayerNameTextField
import com.voxyl.overlay.ui.settings.elements.qol.AutoShowAndHideDelaySlider
import com.voxyl.overlay.ui.settings.elements.sources.PinYourselfToTopCheckbox

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
    LazyColumn(
        state = lazyListState,
        modifier = modifier.absoluteOffset(y = 60.dp).fillMaxSize().scrollbar(lazyListState).requestFocusOnClick(),
        contentPadding = PaddingValues(bottom = 75.dp)
    ) {
        item {
            val addYourself = remember {
                mutableStateOf(Config.getOrNullIfBlank(ADD_YOURSELF_TO_OVERLAY.key)?.toBooleanStrictOrNull() ?: true)
            }

            val autoHide = remember {
                mutableStateOf(Config.getOrNullIfBlank(AUTO_SHOW_AND_HIDE.key)?.toBooleanStrictOrNull() ?: true)
            }

            BWPApiKeyTextField()
            HypixelApiKeyTextField()
            PlayerNameTextField()
            LogFilePathTextField()
            PinYourselfToTopCheckbox(addYourself)
            AddYourselfToOverlayCheckbox(addYourself)
            AutoShowAndHideCheckBox(autoHide)
            AutoShowAndHideDelaySlider(autoHide)
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
            MyText(
                text = text,
                modifier = Modifier.absoluteOffset(y = 6.dp)
            )
        },
        placeholder = {
            MyText(placeholder, fontSize = 12.sp, color = MainWhiteLessOpaque)
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
