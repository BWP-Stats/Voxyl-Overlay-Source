package com.voxyl.overlay.ui.settings.basic

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIconDefaults
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.voxyl.overlay.business.settings.config.Config
import com.voxyl.overlay.business.settings.config.ConfigKeys.HypixelApiKey
import com.voxyl.overlay.ui.elements.VTrailingIcon
import com.voxyl.overlay.ui.settings.SettingsTextField
import com.voxyl.overlay.controllers.common.ui.MainWhite
import com.voxyl.overlay.controllers.common.ui.am


@ExperimentalComposeUiApi
@Composable
fun HypixelApiKeyTextField() {
    var apiKey by remember { mutableStateOf(TextFieldValue()) }

    val doOnEnter = doOnEnter@{
        if (!isValidHypixelApiKey(apiKey)) return@doOnEnter

        Config[HypixelApiKey] = apiKey.text
        apiKey = TextFieldValue()
    }

    SettingsTextField(
        text = getHypixelApiKeyLabel(apiKey, isValidHypixelApiKey(apiKey)),
        placeholder = "'/api new' in Hypixel",
        value = apiKey,
        onValueChange = { apiKey = it },
        doOnEnter = doOnEnter,
        isValid = { apiKey.text.isBlank() || isValidHypixelApiKey(it) },
        trailingIcon = {
            Icon(
                painter = painterResource("icons/eye.png"),
                contentDescription = null,
                modifier = Modifier
                    .offset(x = -18.dp, y = 5.dp)
                    .size(12.dp, 12.dp)
                    .pointerHoverIcon(
                        icon = PointerIconDefaults.Hand
                    )
                    .clickable {
                        apiKey = TextFieldValue(Config.getOrNullIfBlank(HypixelApiKey) ?: "No API key saved")
                    },
                tint = MainWhite.copy(alpha = .313f).am
            )
            VTrailingIcon(
                modifier = Modifier
                    .offset(x = -4.dp, y = 5.dp)
                    .size(12.dp, 12.dp)
            ) {
                if (isValidHypixelApiKey(apiKey)) doOnEnter()
            }
            VTrailingIcon(
                icon = Icons.Filled.Close,
                modifier = Modifier
                    .offset(x = 10.dp, y = 5.dp)
                    .size(12.dp, 12.dp)
            ) {
                Config[HypixelApiKey] = ""
                apiKey = TextFieldValue(" ")
                apiKey = TextFieldValue("")
            }
        }
    )
}

private fun getHypixelApiKeyLabel(apiKey: TextFieldValue, isValid: Boolean) =
    if (apiKey.text.isNotBlank() && !isValid)
        "Please enter a valid API key"
    else if (Config[HypixelApiKey].isBlank())
        "Enter your Hypixel API key"
    else
        "Enter your Hypixel API key (${Config[HypixelApiKey].substring(0, 11) + "*".repeat(22)})"

private fun isValidHypixelApiKey(tfv: TextFieldValue) =
    tfv.text.matches(Regex("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}"))