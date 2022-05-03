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
import com.voxyl.overlay.config.ConfigKeys.PLAYER_NAME
import com.voxyl.overlay.ui.common.elements.MyTrailingIcon
import com.voxyl.overlay.ui.settings.elements.SettingsTextField
import com.voxyl.overlay.ui.theme.MainWhiteLessOpaque
import java.io.File

@ExperimentalComposeUiApi
@Composable
fun PlayerNameTextField() {
    var name by remember { mutableStateOf(TextFieldValue()) }

    val doOnEnter = doOnEnter@{
        if (!isValidName(name)) return@doOnEnter

        Config[PLAYER_NAME] = name.text
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
                bitmap = loadImageBitmap(File("src/main/resources/icons/eye.png").inputStream()),
                contentDescription = null,
                modifier = Modifier
                    .offset(x = (-4).dp, y = 5.dp)
                    .size(12.dp, 12.dp)
                    .pointerHoverIcon(
                        icon = PointerIconDefaults.Hand
                    )
                    .clickable {
                        name = TextFieldValue(Config.getOrNullIfBlank(PLAYER_NAME) ?: "No name saved")
                    },
                tint = MainWhiteLessOpaque
            )
            MyTrailingIcon(
                modifier = Modifier
                    .offset(x = 10.dp, y = 5.dp)
                    .size(12.dp, 12.dp)
            ) {
                if (isValidName(name)) doOnEnter()
            }
        }
    )
}

private fun getNameLabel(name: TextFieldValue, isValid: Boolean) =
    if (name.text.isNotBlank() && !isValid)
        "Please enter a valid name"
    else if (Config[PLAYER_NAME.key]?.isBlank() == true)
        "Enter your MC username"
    else
        "Enter your MC username (${Config[PLAYER_NAME.key]})"

private fun isValidName(tfv: TextFieldValue) = tfv.text.matches(Regex("\\w{1,16}"))