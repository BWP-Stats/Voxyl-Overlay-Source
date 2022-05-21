package com.voxyl.overlay.dataslashbusiness.logfilereader

import com.voxyl.overlay.Window
import com.voxyl.overlay.kindasortasomewhatviewmodelsishiguessithinkidkwhatevericantbebotheredsmh.EventsToBeDisplayed
import com.voxyl.overlay.settings.config.Config
import com.voxyl.overlay.settings.config.ConfigKeys.LogFilePath
import com.voxyl.overlay.settings.config.ConfigKeys.AutoShowAndHide
import com.voxyl.overlay.settings.config.ConfigKeys.AutoShowAndHideDelay
import com.voxyl.overlay.kindasortasomewhatviewmodelsishiguessithinkidkwhatevericantbebotheredsmh.PlayerKindaButNotExactlyViewModel
import com.voxyl.overlay.dataslashbusiness.events.Error
import com.voxyl.overlay.dataslashbusiness.player.Tags
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.FileNotFoundException

object LogFileReader {

    var job: Job? = null

    @OptIn(DelicateCoroutinesApi::class)
    fun start(cs: CoroutineScope = GlobalScope) = cs.launch(Dispatchers.IO) {
        job?.cancel()

        val reader = Config.getOrNullIfBlank(LogFilePath)?.let {
            try {
                FileInputStream(it).bufferedReader(Charsets.UTF_8)
            } catch (e: FileNotFoundException) {
                null
            }
        } ?: return@launch Unit.also {
            EventsToBeDisplayed.add(Error("Error starting log file reader: Log file path may be invalid or inaccessable.", 10000))
        }

        read(reader, cs)
    }

    private fun read(reader: BufferedReader, cs: CoroutineScope) {
        job = cs.launch(Dispatchers.IO) {
            skipToEndOfFile(reader)

            while (true) {
                if (!isActive) return@launch

                val line = reader.readLine()

                if (line != null) {
                    interpret(line, cs)
                }
            }
        }
    }

    private fun skipToEndOfFile(reader: BufferedReader) = reader.skip(Long.MAX_VALUE)

    private fun interpret(line: String, cs: CoroutineScope) {
        checkForGameStart(line, cs)
    }

    private fun checkForGameStart(line: String, cs: CoroutineScope) {
        if (" [CHAT] Players in this game: " !in line) return

        PlayerKindaButNotExactlyViewModel.removeAll()

        line.substringAfterLast(":").toPlayerList().forEach {
            PlayerKindaButNotExactlyViewModel.removeAll()
            PlayerKindaButNotExactlyViewModel.add(it, cs, Tags.FromGame)
        }

        if (Config[AutoShowAndHide] == "true") {
            cs.launch(Dispatchers.Default) {
                Window.isAlwaysOnTop = true
                delay(Config[AutoShowAndHideDelay].toLongOrNull() ?: 5000)
                Window.isMinimized = true
            }
        }
    }

    private fun String.toPlayerList() = split(" ").filterNot { it.isBlank() }.distinct()
}
