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
import androidx.compose.ui.input.key.*
import androidx.compose.ui.input.pointer.PointerIconDefaults
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.voxyl.overlay.business.logfilereader.LogFileReader
import com.voxyl.overlay.business.settings.config.Config
import com.voxyl.overlay.business.settings.config.LogFilePath
import com.voxyl.overlay.business.settings.config.LogFiles
import com.voxyl.overlay.controllers.common.ui.MainWhite
import com.voxyl.overlay.controllers.common.ui.am
import com.voxyl.overlay.ui.elements.VTrailingIcon
import com.voxyl.overlay.ui.settings.SettingsTextField

@ExperimentalComposeUiApi
@Composable
fun LogFilePathTextField() {
    var logFilePath by remember { mutableStateOf(TextFieldValue()) }

    val doOnEnter = doOnEnter@{
        if (!isValidLogFilePath(logFilePath)) return@doOnEnter

        Config[LogFilePath] = logFilePath.text
        logFilePath = TextFieldValue()
    }

    SettingsTextField(
        modifier = Modifier.onPreviewKeyEvent {
            when {
                (it.key == Key.Tab && it.type == KeyEventType.KeyUp) -> {
                    autofillLogFilePath(logFilePath) {
                        logFilePath = TextFieldValue(it)
                    }
                    true
                }
                else -> false
            }
        },
        text = getLogFilePathLabel(logFilePath, isValidLogFilePath(logFilePath)),
        value = logFilePath,
        onValueChange = { logFilePath = it },
        doOnEnter = doOnEnter,
        isValid = { logFilePath.text.isBlank() || isValidLogFilePath(it) },
        placeholder = "Type in 'badlion', 'lunar', or 'forge/feather/vanilla' then `tab` to autofill common log file paths",
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
                        logFilePath = TextFieldValue(Config.getOrNullIfBlank(LogFilePath) ?: "No LogFilePath saved")
                    },
                tint = MainWhite.copy(alpha = .313f).am
            )
            VTrailingIcon(
                modifier = Modifier
                    .offset(x = -4.dp, y = 5.dp)
                    .size(12.dp, 12.dp)
            ) {
                if (isValidLogFilePath(logFilePath)) doOnEnter()
            }
            VTrailingIcon(
                icon = Icons.Filled.Close,
                modifier = Modifier
                    .offset(x = 10.dp, y = 5.dp)
                    .size(12.dp, 12.dp)
            ) {
                Config[LogFilePath] = ""
                logFilePath = TextFieldValue(" ")
                logFilePath = TextFieldValue("")
            }
        }
    )
}

private fun autofillLogFilePath(logFilePath: TextFieldValue, autoFillLogPath: (String) -> Unit) {
    val autofilledFilePath = when (logFilePath.text.lowercase()) {
        "badlion" -> LogFiles.Badlion.path
        "lunar" -> LogFiles.Lunar.path
        "vanilla", "forge", "feather", "labymod" -> LogFiles.Vanilla.path
        else -> logFilePath.text
    }
    autoFillLogPath(autofilledFilePath)
}

private fun getLogFilePathLabel(logFilePath: TextFieldValue, isValid: Boolean) =
    if (logFilePath.text.isNotBlank() && !isValid)
        "Please enter a valid log file path"
    else if (Config[LogFilePath].isBlank())
        "Enter your log file path"
    else
        "Enter your log file path (${Config[LogFilePath]})"

private fun isValidLogFilePath(tfv: TextFieldValue) = tfv.text.contains(".log")
