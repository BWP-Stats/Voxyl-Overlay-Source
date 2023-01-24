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
import com.voxyl.overlay.business.settings.config.*
import com.voxyl.overlay.ui.elements.VTrailingIcon
import com.voxyl.overlay.ui.settings.SettingsTextField
import com.voxyl.overlay.controllers.common.ui.MainWhite
import com.voxyl.overlay.controllers.common.ui.am

@ExperimentalComposeUiApi
@Composable
fun PlayerNameTextField() {
    var name by remember { mutableStateOf(TextFieldValue()) }

    val doOnEnter = doOnEnter@{
        if (!isValidName(name)) return@doOnEnter

        Config[PlayerName] = name.text
        name = TextFieldValue()
    }

    SettingsTextField(
        text = getNameLabel(name, isValidName(name)),
        value = name,
        onValueChange = { name = it },
        doOnEnter = doOnEnter,
        isValid = { name.text.isBlank() || isValidName(it) },
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
                        name = TextFieldValue(Config.getOrNullIfBlank(PlayerName) ?: "No name saved")
                    },
                tint = MainWhite.copy(alpha = .313f).am
            )
            VTrailingIcon(
                modifier = Modifier
                    .offset(x = -4.dp, y = 5.dp)
                    .size(12.dp, 12.dp)
            ) {
                if (isValidName(name)) doOnEnter()
            }
            VTrailingIcon(
                icon = Icons.Filled.Close,
                modifier = Modifier
                    .offset(x = 10.dp, y = 5.dp)
                    .size(12.dp, 12.dp)
            ) {
                Config[PlayerName] = ""
                name = TextFieldValue(" ")
                name = TextFieldValue("")
            }
        }
    )
}

private fun getNameLabel(name: TextFieldValue, isValid: Boolean) =
    if (name.text.isNotBlank() && !isValid)
        "Please enter a valid name"
    else if (Config[PlayerName].isBlank())
        "Enter your MC username"
    else
        "Enter your MC username (${Config[PlayerName]})"

private fun isValidName(tfv: TextFieldValue) = tfv.text.matches(Regex("\\w{1,16}"))
