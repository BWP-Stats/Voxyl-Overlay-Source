package com.voxyl.overlay.ui.settings.backup

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.voxyl.overlay.business.settings.Settings
import com.voxyl.overlay.controllers.common.ui.ErrorColor
import com.voxyl.overlay.controllers.common.ui.MainWhite
import com.voxyl.overlay.controllers.common.ui.am
import com.voxyl.overlay.ui.elements.VText
import java.io.File

@Composable
fun BackupButtons() {
    var backupExists by remember { mutableStateOf(Settings.backupExists()) }

    Row(
        modifier = Modifier
            .height(32.dp)
            .fillMaxWidth()
            .padding(horizontal = 19.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        BackupButton("Backup settings") {
            Settings.backupToTemp()
            backupExists = Settings.backupExists()
        }

        BackupButton(
            text = "Restore settings",
            color = if (backupExists) MainWhite else ErrorColor,
            enabled = backupExists,
        ) {
            Settings.restoreFromTemp()
        }

        BackupButton("Delete backup") {
            Settings.clearBackups()
            backupExists = Settings.backupExists()
        }

        OpenFileExplorerButton()
    }
}

@Composable
private fun RowScope.BackupButton(
    text: String,
    color: Color = MainWhite,
    enabled: Boolean = true,
    onPressed: () -> Unit
) {
    var pressed by remember { mutableStateOf(false) }

    val animSpec = TweenSpec<Float>(if (pressed) 875 else 175, easing = LinearEasing)

    val progress by animateFloatAsState(if (pressed && enabled) 1f else 0f, animSpec) {
        if (enabled && it >= .95) {
            onPressed()
            pressed = false
        }
    }

    Box(
        modifier = Modifier
            .padding(horizontal = 5.dp)
            .background(Color(0f, 0f, 0f, .3f).am)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        pressed = true
                        tryAwaitRelease()
                        pressed = false
                    }
                )
            }
            .weight(1f)
            .fillMaxHeight(),
        contentAlignment = Alignment.Center
    ) {
        VText(
            text = text,
            modifier = Modifier.padding(horizontal = 10.dp),
            color = color,
        )

        ProgressBarBox(progress)
    }
}

@Composable
private fun BoxScope.ProgressBarBox(progress: Float) = Box(
    modifier = Modifier
        .align(Alignment.CenterStart)
        .background(Color(.8f, .8f, .8f, .3f).am)
        .fillMaxHeight()
        .fillMaxWidth(progress),
)

@Composable
private fun RowScope.OpenFileExplorerButton() = Box(
    modifier = Modifier
        .padding(horizontal = 5.dp)
        .background(Color(0f, 0f, 0f, .3f).am)
        .clickable {
            val settingsFolderPath = File(Settings.BASE_PATH).absolutePath
            Runtime.getRuntime().exec("explorer $settingsFolderPath")
        }
        .weight(1.25f)
        .fillMaxHeight(),
    contentAlignment = Alignment.Center
) {
    VText(
        text = "Open settings folder",
        modifier = Modifier.padding(horizontal = 10.dp)
    )
}
