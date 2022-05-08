package com.voxyl.overlay.data.logfilereader

import com.voxyl.overlay.Window
import com.voxyl.overlay.settings.config.Config
import com.voxyl.overlay.settings.config.ConfigKeys.LogFilePath
import com.voxyl.overlay.settings.config.ConfigKeys.AutoShowAndHide
import com.voxyl.overlay.settings.config.ConfigKeys.AutoShowAndHideDelay
import com.voxyl.overlay.middleman.PlayerKindaButNotExactlyViewModel
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.FileNotFoundException

//Voyx qoodles MaxFrostbite11 mrGuineapigze1 Jaded_Lord carburettor Speed1200 _lightninq ambmt lansraad anoninho
object LogFileReader {

    var job: Job? = null

    fun start(cs: CoroutineScope) = cs.launch(Dispatchers.IO) {
        job?.cancel()

        val reader = Config.getOrNullIfBlank(LogFilePath)?.let {
            try {
                FileInputStream(it).bufferedReader(Charsets.UTF_8)
            } catch (e: FileNotFoundException) {
                null
            }
        } ?: return@launch

        read(reader, cs)
    }

    private fun read(reader: BufferedReader, cs: CoroutineScope) {
        job = cs.launch(Dispatchers.IO) {
            skipToEndOfFile(reader)

            while (true) {
                if (!isActive) return@launch

                val line = reader.readLine()

                if (line != null) {
                    //println(line)
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

        PlayerKindaButNotExactlyViewModel.clear()

        line.substringAfterLast(":").toPlayerList().forEach {
            PlayerKindaButNotExactlyViewModel.add(it, cs)
        }

        if (Config[AutoShowAndHide] == "true") {
            cs.launch {
                Window.isAlwaysOnTop = true
                delay(Config[AutoShowAndHideDelay].toLongOrNull() ?: 5000)
                Window.isMinimized = true
            }
        }
    }

    private fun String.toPlayerList() = split(" ").filterNot { it.isBlank() }.distinct()
}
