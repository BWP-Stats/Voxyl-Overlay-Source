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
import com.github.kwhat.jnativehook.GlobalScreen
import com.voxyl.overlay.data.homemadesimplecache.HomemadeCache
import com.voxyl.overlay.data.logfilereader.LogFileReader
import com.voxyl.overlay.middleman.LeaderboardTrackerWhatEvenIsAViewModel
import com.voxyl.overlay.nativelisteners.NativeListeners
import com.voxyl.overlay.nativelisteners.OpenCloseKeyListener
import com.voxyl.overlay.settings.Settings
import com.voxyl.overlay.settings.logger.DefaultHandler
import com.voxyl.overlay.settings.window.SavedWindowState
import com.voxyl.overlay.settings.window.SavedWindowStateKeys.*
import com.voxyl.overlay.ui.common.MainScreen
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import kotlinx.coroutines.Dispatchers
import java.awt.Dimension
import java.util.logging.*

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
        alwaysOnTop = SavedWindowState[IsAlwaysOnTop].toBooleanStrictOrNull() == true,
        state = rememberWindowState(
            size = getPreferredWindowSize(),
            position = getPreferredWindowPosition()
        )
    ) {
        Window = window
        window.minimumSize = Dimension(400, 200)

        val cs = rememberCoroutineScope()

        LeaderboardTrackerWhatEvenIsAViewModel.startTracking(cs)
        HomemadeCache.startAutoClear(cs)
        LogFileReader.start(cs)
        Napier.initialize()
        NativeListeners.initialize()

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

private fun Napier.initialize() {
    val consoleHandler = ConsoleHandler().apply {
        level = Level.ALL
        formatter = SimpleFormatter()
    }

    base(DebugAntilog(handler = listOf(DefaultHandler.`😳`, consoleHandler)))
}
