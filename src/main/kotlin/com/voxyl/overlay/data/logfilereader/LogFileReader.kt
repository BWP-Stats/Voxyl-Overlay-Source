package com.voxyl.overlay.data.logfilereader

import androidx.compose.ui.window.FrameWindowScope
import com.voxyl.overlay.config.Config
import com.voxyl.overlay.config.Config.Keys.*
import com.voxyl.overlay.middleman.PlayerKindaButNotExactlyViewModel
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.FileInputStream

//Voyx qoodles MaxFrostbite11 mrGuineapigze1 Jaded_Lord carburettor Speed1200 _lightninq ambmt lansraad anoninho
object LogFileReader {

    lateinit var window: FrameWindowScope

    suspend fun start(cs: CoroutineScope, _window: FrameWindowScope) = withContext(Dispatchers.IO) {
        window = _window

        val reader = Config[LOG_FILE_PATH.key]?.let {
            try {
                FileInputStream(it).bufferedReader(Charsets.UTF_8)
            } catch (e: Exception) {
                return@withContext
            }
        } ?: return@withContext

        read(reader, cs)
    }

    private suspend fun read(reader: BufferedReader, cs: CoroutineScope) = withContext(Dispatchers.IO) {
        skipToEndOfFile(reader)

        while (true) {
            val line = reader.readLine()

            if (line != null) {
                //println(line)
                interpret(line, cs)
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

        cs.launch {
            line.substringAfterLast(":").toPlayerList().forEach {
                PlayerKindaButNotExactlyViewModel.add(it, cs)
            }
        }

        if (Config[AUTO_SHOW_AND_HIDE.key] == "true") {
            cs.launch {
                window.window.isAlwaysOnTop = true
                delay(Config[AUTO_SHOW_AND_HIDE_DELAY.key]?.toLongOrNull() ?: 5000)
                window.window.isMinimized = true
            }
        }
    }
}

fun String.toPlayerList() = split(" ").filterNot { it.isBlank() }.distinct()
