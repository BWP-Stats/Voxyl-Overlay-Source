package com.voxyl.overlay

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import com.voxyl.overlay.business.autoupdater.UpdateChecker
import com.voxyl.overlay.business.logfilereader.LogFileReader
import com.voxyl.overlay.kindasortasomewhatviewmodelsishiguessithinkidkwhatevericantbebotheredsmh.PopUpQueue
import com.voxyl.overlay.kindasortasomewhatviewmodelsishiguessithinkidkwhatevericantbebotheredsmh.LeaderboardTrackerWhatEvenIsAViewModel
import com.voxyl.overlay.business.nativelisteners.NativeListeners
import com.voxyl.overlay.business.validation.ValidationChecks
import com.voxyl.overlay.appinfo.AppInfo
import com.voxyl.overlay.settings.Settings
import com.voxyl.overlay.settings.window.SavedWindowState
import com.voxyl.overlay.settings.window.SavedWindowStateKeys.*
import com.voxyl.overlay.ui.common.MainScreen
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import java.awt.Dimension
import java.io.File
import java.util.logging.ConsoleHandler
import java.util.logging.FileHandler
import java.util.logging.Level
import java.util.logging.SimpleFormatter

lateinit var Window: ComposeWindow
    private set

@ExperimentalComposeUiApi
@Preview
fun main() = application {
    val windowState = rememberWindowState(
        position = getPreferredWindowPosition(),
        size = getPreferredWindowSize()
    )

    Window(
        onCloseRequest = {
            Settings.storeAll()
            exitApplication()
        },
        undecorated = true,
        transparent = true,
        title = "Voxyl Overlay",
        icon = painterResource("VoxylLogoIcon.ico"),
        alwaysOnTop = SavedWindowState[IsAlwaysOnTop] == "true",
        state = windowState
    ) {
        Window = window.apply {
            minimumSize = Dimension(600, 200)
        }

        val cs = rememberCoroutineScope()

        LaunchedEffect(Unit) {
            LeaderboardTrackerWhatEvenIsAViewModel.startTracking()
            ValidationChecks.runAtStart(cs)
            NativeListeners.initialize()
            PopUpQueue.start()
            Napier.initialize()
            LogFileReader.start()
            UpdateChecker.check(cs)
        }

        VTray(windowState)
        MainScreen()
    }
}

@Composable
fun ApplicationScope.VTray(windowState: WindowState) = Tray(
    icon = painterResource("VoxylLogoIcon.ico"),
    tooltip = "Voxyl Overlay",
) {
    val cs = rememberCoroutineScope()

    Item("Voxyl Overlay (v${AppInfo.VERSION})") {}

    Separator()

    Item("Reset position") {
        windowState.position = WindowPosition(0.dp, 0.dp)
    }

    Item("Check for updates") {
        UpdateChecker.check(cs, true)
    }

    Separator()

    Item("Exit") {
        Settings.storeAll()
        exitApplication()
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

@Suppress("ObjectPropertyName", "NonAsciiCharacters", "MemberVisibilityCanBePrivate")
object DefaultHandler {
    val path = getPath()

    val `😳` = FileHandler(path, 2000000, 5, true).apply {
        level = Level.ALL
        formatter = SimpleFormatter()
    }

    @JvmName("getPath1")
    private fun getPath(path: String = "./logs/voxyl%g.log"): String {
        val configFile = File(path)
        configFile.parentFile.mkdirs()
        return configFile.absolutePath
    }
}