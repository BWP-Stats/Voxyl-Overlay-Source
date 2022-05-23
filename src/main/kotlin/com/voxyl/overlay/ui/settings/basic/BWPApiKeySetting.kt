package com.voxyl.overlay.ui.settings.basic

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIconDefaults
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.voxyl.overlay.settings.config.Config
import com.voxyl.overlay.settings.config.ConfigKeys.BwpApiKey
import com.voxyl.overlay.ui.elements.MyTrailingIcon
import com.voxyl.overlay.ui.settings.SettingsTextField
import com.voxyl.overlay.ui.theme.MainWhite
import com.voxyl.overlay.ui.theme.am

@ExperimentalComposeUiApi
@Composable
fun BWPApiKeyTextField() {
    var apiKey by remember { mutableStateOf(TextFieldValue()) }

    val doOnEnter = doOnEnter@{
        if (!isValidBwpApiKey(apiKey)) return@doOnEnter

        Config[BwpApiKey] = apiKey.text
        apiKey = TextFieldValue()
    }

    SettingsTextField(
        text = getBwpApiKeyLabel(apiKey, isValidBwpApiKey(apiKey)),
        placeholder = "'/api new' or '/api get' in BWP",
        value = apiKey,
        onValueChange = { apiKey = it },
        doOnEnter = doOnEnter,
        isValid = { apiKey.text.isBlank() || isValidBwpApiKey(it) },
        trailingIcon = {
            Icon(
                painter = painterResource("icons/eye.png"),
                contentDescription = null,
                modifier = Modifier
                    .offset(x = (-4).dp, y = 5.dp)
                    .size(12.dp, 12.dp)
                    .pointerHoverIcon(
                        icon = PointerIconDefaults.Hand
                    )
                    .clickable {
                        apiKey = TextFieldValue(Config.getOrNullIfBlank(BwpApiKey) ?: "No API key saved")
                    },
                tint = MainWhite.copy(alpha = .313f).am
            )
            MyTrailingIcon(
                modifier = Modifier
                    .offset(x = 10.dp, y = 5.dp)
                    .size(12.dp, 12.dp)
            ) {
                if (isValidBwpApiKey(apiKey)) doOnEnter()
            }
        }
    )
}

fun getBwpApiKeyLabel(apiKey: TextFieldValue, isValid: Boolean) =
    if (apiKey.text.isNotBlank() && !isValid)
        "API key must be 32 chars and contain only letters & numbers"
    else if (Config[BwpApiKey].isBlank())
        "Enter your BWP API key"
    else
        "Enter your BWP API key (${Config[BwpApiKey].substring(0, 11) + "*".repeat(22)})"

fun isValidBwpApiKey(tfv: TextFieldValue) = tfv.text.matches(Regex("[a-zA-Z0-9]{32}"))