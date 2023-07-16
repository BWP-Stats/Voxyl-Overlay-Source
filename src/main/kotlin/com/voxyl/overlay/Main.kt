package com.voxyl.overlay

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalContext
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import com.voxyl.overlay.appinfo.AppInfo
import com.voxyl.overlay.business.autoupdater.AutoUpdater
import com.voxyl.overlay.business.autoupdater.UpdateChecker
import com.voxyl.overlay.business.discordrpc.DiscordRPC
import com.voxyl.overlay.business.logfilereader.LogFileReader
import com.voxyl.overlay.business.nativelisteners.NativeListeners
import com.voxyl.overlay.business.settings.Settings
import com.voxyl.overlay.business.settings.config.Config
import com.voxyl.overlay.business.settings.config.ShowDiscordRP
import com.voxyl.overlay.business.settings.window.*
import com.voxyl.overlay.business.settings.window.WindowState
import com.voxyl.overlay.business.validation.ValidationChecks
import com.voxyl.overlay.business.validation.popups.Warning
import com.voxyl.overlay.controllers.common.PopUpQueue
import com.voxyl.overlay.controllers.playerstats.LeaderboardTracker
import com.voxyl.overlay.ui.common.MainScreen
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.reflections.Reflections
import org.reflections.scanners.Scanners
import org.reflections.util.ClasspathHelper
import org.reflections.util.ConfigurationBuilder
import java.awt.Dimension
import java.io.File
import java.util.logging.ConsoleHandler
import java.util.logging.FileHandler
import java.util.logging.Level
import java.util.logging.SimpleFormatter
import kotlin.system.measureTimeMillis
import androidx.compose.ui.window.WindowState as AndroidWindowState

lateinit var AppWindow: ComposeWindow
    private set

@Preview
@ExperimentalComposeUiApi
fun main() = application {
    Settings.loadAll()

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
        icon = painterResource("logos/VoxylLogoIcon.ico"),
        alwaysOnTop = WindowState[IsAlwaysOnTop] == "true",
        state = windowState
    ) {
        AppWindow = window.apply {
            minimumSize = Dimension(600, 200)
        }

        val cs = rememberCoroutineScope()

        LaunchedEffect(Unit) {
            PopUpQueue.start()

            val restoring = Settings.checkForAutoRestore()

            if (restoring) {
                PopUpQueue.add(Warning("Old settings found; restoring. (Trying to) restart overlay. Reopen if restart fails.", 3000L))

                Settings.storeAll()

                Runtime.getRuntime().addShutdownHook(Thread {
                    try {
                        Runtime.getRuntime().exec("VoxylOverlay", null, File("."))
                    } catch (_: Exception) {}
                })

                delay(3150)

                exitApplication()
            }

            ValidationChecks.runAtStart()
            Napier.initialize()
            NativeListeners.initialize()
            LogFileReader.start()
            UpdateChecker.check(cs)
            LeaderboardTracker.startTracking()

            if (Config[ShowDiscordRP] != "false") {
                DiscordRPC.start(cs)
            }
        }

        VTray(windowState)
        MainScreen()
    }
}

@Composable
fun ApplicationScope.VTray(windowState: AndroidWindowState) = Tray(
    icon = painterResource("logos/VoxylLogoIcon.ico"),
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

    Item("Refresh Discord RPC") {
        DiscordRPC.refresh(cs)
    }

    Separator()

    Item("Exit") {
        Settings.storeAll()
        exitApplication()
    }
}

fun getPreferredWindowPosition(): WindowPosition {
    val x = WindowState[X].toFloatOrNull() ?: return WindowPosition.PlatformDefault
    val y = WindowState[Y].toFloatOrNull() ?: return WindowPosition.PlatformDefault

    return WindowPosition(x.dp, y.dp)
}

fun getPreferredWindowSize(): DpSize {
    val w = WindowState[Width].toFloatOrNull() ?: return DpSize(650.dp, 300.dp)
    val h = WindowState[Height].toFloatOrNull() ?: return DpSize(650.dp, 300.dp)

    return DpSize(w.dp, h.dp)
}

private fun Napier.initialize() {
    val consoleHandler = ConsoleHandler().apply {
        level = Level.ALL
        formatter = SimpleFormatter()
    }

    base(DebugAntilog(handler = listOf(DefaultHandler.get(), consoleHandler)))
}

object DefaultHandler {
    fun get() = handler

    private val handler = FileHandler(getPath(), 2000000, 5, true).apply {
        level = Level.ALL
        formatter = SimpleFormatter()
    }

    private fun getPath(path: String = "./logs/voxyl%g.log"): String {
        val configFile = File(path)
        configFile.parentFile.mkdirs()
        return configFile.absolutePath
    }
}
