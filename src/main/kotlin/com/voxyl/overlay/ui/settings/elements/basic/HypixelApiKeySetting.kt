package com.voxyl.overlay.ui.settings.elements.basic

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIconDefaults
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.voxyl.overlay.config.Config
import com.voxyl.overlay.config.ConfigKeys.HYPIXEL_API_KEY
import com.voxyl.overlay.ui.common.elements.MyTrailingIcon
import com.voxyl.overlay.ui.settings.elements.SettingsTextField
import com.voxyl.overlay.ui.theme.MainWhiteLessOpaque
import java.io.File

@ExperimentalComposeUiApi
@Composable
fun HypixelApiKeyTextField() {
    var apiKey by remember { mutableStateOf(TextFieldValue()) }

    val doOnEnter = doOnEnter@{
        if (!isValidHypixelApiKey(apiKey)) return@doOnEnter

        Config[HYPIXEL_API_KEY] = apiKey.text
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
                bitmap = loadImageBitmap(File("src/main/resources/icons/eye.png").inputStream()),
                contentDescription = null,
                modifier = Modifier
                    .offset(x = (-4).dp, y = 5.dp)
                    .size(12.dp, 12.dp)
                    .pointerHoverIcon(
                        icon = PointerIconDefaults.Hand
                    )
                    .clickable {
                        apiKey = TextFieldValue(Config.getOrNullIfBlank(HYPIXEL_API_KEY) ?: "No API key saved")
                    },
                tint = MainWhiteLessOpaque
            )
            MyTrailingIcon(
                modifier = Modifier
                    .offset(x = 10.dp, y = 5.dp)
                    .size(12.dp, 12.dp)
            ) {
                if (isValidHypixelApiKey(apiKey)) doOnEnter()
            }
        }
    )
}

private fun getHypixelApiKeyLabel(apiKey: TextFieldValue, isValid: Boolean) =
    if (apiKey.text.isNotBlank() && !isValid)
        "Please enter a valid API key"
    else if (Config[HYPIXEL_API_KEY].isBlank())
        "Enter your Hypixel API key"
    else
        "Enter your Hypixel API key (${Config[HYPIXEL_API_KEY].substring(0, 11) + "*".repeat(22)})"

private fun isValidHypixelApiKey(tfv: TextFieldValue) =
    tfv.text.matches(Regex("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}"))