package com.voxyl.overlay.ui.settings.elements.basic

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.*
import androidx.compose.ui.input.pointer.PointerIconDefaults
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.voxyl.overlay.config.Config
import com.voxyl.overlay.config.ConfigKeys.LOG_FILE_PATH
import com.voxyl.overlay.ui.common.elements.MyTrailingIcon
import com.voxyl.overlay.ui.settings.elements.SettingsTextField
import com.voxyl.overlay.ui.theme.MainWhiteLessOpaque
import java.io.File

@ExperimentalComposeUiApi
@Composable
fun LogFilePathTextField() {
    var logFilePath by remember { mutableStateOf(TextFieldValue()) }

    val doOnEnter = doOnEnter@{
        if (!isValidLogFilePath(logFilePath)) return@doOnEnter

        Config[LOG_FILE_PATH] = logFilePath.text
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
        placeholder = "Type in 'badlion', 'lunar', or 'forge/pvplounge/vanilla' then `tab` to autofill common log file paths",
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
                        logFilePath = TextFieldValue(Config.getOrNullIfBlank(LOG_FILE_PATH) ?: "No LogFilePath saved")
                    },
                tint = MainWhiteLessOpaque
            )
            MyTrailingIcon(
                modifier = Modifier
                    .offset(x = 10.dp, y = 5.dp)
                    .size(12.dp, 12.dp)
            ) {
                if (isValidLogFilePath(logFilePath)) doOnEnter()
            }
        }
    )
}

fun autofillLogFilePath(logFilePath: TextFieldValue, autoFillLogPath: (String) -> Unit) {
    val autofilledFilePath = when (logFilePath.text) {
        "badlion" -> "badlion/logs/badlion.log"
        "lunar" -> "lunar/logs/lunar.log"
        "vanilla", "forge", "pvplounge" -> "vanilla/logs/vanilla.log"
        else -> logFilePath.text
    }
    autoFillLogPath(autofilledFilePath)
}

fun getLogFilePathLabel(LogFilePath: TextFieldValue, isValid: Boolean) =
    if (LogFilePath.text.isNotBlank() && !isValid)
        "Please enter a valid log file path"
    else if (Config[LOG_FILE_PATH].isBlank())
        "Enter your log file path"
    else
        "Enter your log file path (${Config[LOG_FILE_PATH]})"

fun isValidLogFilePath(tfv: TextFieldValue) = tfv.text.contains(".log")