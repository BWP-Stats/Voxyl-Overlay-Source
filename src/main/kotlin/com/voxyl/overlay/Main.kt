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
import com.voxyl.overlay.data.logfilereader.LogFileReader
import com.voxyl.overlay.settings.Settings
import com.voxyl.overlay.settings.window.SavedWindowState
import com.voxyl.overlay.settings.window.SavedWindowStateKeys.*
import com.voxyl.overlay.ui.common.MainScreen
import kotlinx.coroutines.Dispatchers
import java.awt.Dimension
import java.util.*

lateinit var Window: ComposeWindow
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
        title = "Voxyl Overlay",
        icon = painterResource("VoxylLogoIcon.ico"),
        alwaysOnTop = SavedWindowState[IsAlwaysOnTop].toBooleanStrictOrNull() ?: false,
        state = rememberWindowState(
            size = getPreferredWindowSize(),
            position = getPreferredWindowPosition()
        )
    ) {
        Window = window
        window.minimumSize = Dimension(400, 200)

        val cs = rememberCoroutineScope { Dispatchers.IO }

        LaunchedEffect(Unit) {
            LogFileReader.start(cs)
        }

        MainScreen(this)
    }
}

fun getPreferredWindowPosition(): WindowPosition {
    val x = SavedWindowState[X].toFloatOrNull() ?: return WindowPosition.PlatformDefault
    val y = SavedWindowState[Y].toFloatOrNull() ?: return WindowPosition.PlatformDefault

    return WindowPosition(x.dp, y.dp)
}

fun getPreferredWindowSize(): DpSize {
    val w = SavedWindowState[Width].toFloatOrNull() ?: return DpSize(650.dp, 300.dp)
    val h = SavedWindowState[Height].toFloatOrNull() ?: return DpSize(650.dp, 300.dp)

    return DpSize(w.dp, h.dp)
}
