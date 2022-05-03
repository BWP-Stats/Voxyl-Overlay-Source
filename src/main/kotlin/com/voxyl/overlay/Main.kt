@file:Suppress("FunctionName", "ObjectPropertyName")
@file:OptIn(ExperimentalComposeUiApi::class)

package com.voxyl.overlay

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.voxyl.overlay.config.SavedWindowState
import com.voxyl.overlay.config.SavedWindowStateKeys.*
import com.voxyl.overlay.config.Settings
import com.voxyl.overlay.data.logfilereader.LogFileReader
import com.voxyl.overlay.ui.common.MainScreen
import kotlinx.coroutines.Dispatchers
import java.awt.Dimension

lateinit var WINDOW: ComposeWindow
    private set

@ExperimentalComposeUiApi
@Preview
fun main() = application {

    Window(
        onCloseRequest = {
            Settings.storeAll()
            exitApplication()
        },
        undecorated = true,
        transparent = true,
        title = "VoxylOverlay",
        icon = painterResource("VoxylLogoIcon.ico"),
        state = rememberWindowState(
            size = getPreferredWindowSize(),
            position = getPreferredWindowPosition()
        )
    ) {
        WINDOW = window
        window.minimumSize = Dimension(400, 200)

        val cs = rememberCoroutineScope { Dispatchers.IO }

        LaunchedEffect(Unit) {
            LogFileReader.start(cs)
        }

        MainScreen(this)
    }
}

//TODO: Return window position from previous usage
fun getPreferredWindowPosition(): WindowPosition {
    val x = SavedWindowState[SAVED_X].toFloatOrNull() ?: return WindowPosition.PlatformDefault
    val y = SavedWindowState[SAVED_Y].toFloatOrNull() ?: return WindowPosition.PlatformDefault

    println("x: $x, y: $y v1")

    return WindowPosition(x.dp, y.dp)
}

//TODO: Return window size from previous usage
fun getPreferredWindowSize(): DpSize {
    val w = SavedWindowState[SAVED_WIDTH].toFloatOrNull() ?: return DpSize(650.dp, 300.dp)
    val h = SavedWindowState[SAVED_HEIGHT].toFloatOrNull() ?: return DpSize(650.dp, 300.dp)

    println("w: $w, h: $h v2")

    return DpSize(w.dp, h.dp)
}
