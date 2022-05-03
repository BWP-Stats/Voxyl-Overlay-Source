package com.voxyl.overlay.data.logfilereader

import com.voxyl.overlay.WINDOW
import com.voxyl.overlay.config.Config
import com.voxyl.overlay.config.ConfigKeys.LOG_FILE_PATH
import com.voxyl.overlay.config.ConfigKeys.AUTO_SHOW_AND_HIDE
import com.voxyl.overlay.config.ConfigKeys.AUTO_SHOW_AND_HIDE_DELAY
import com.voxyl.overlay.middleman.PlayerKindaButNotExactlyViewModel
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.FileNotFoundException

//Voyx qoodles MaxFrostbite11 mrGuineapigze1 Jaded_Lord carburettor Speed1200 _lightninq ambmt lansraad anoninho
object LogFileReader {


    suspend fun start(cs: CoroutineScope) = withContext(Dispatchers.IO) {

        val reader = Config.getOrNullIfBlank(LOG_FILE_PATH)?.let {
            try {
                FileInputStream(it).bufferedReader(Charsets.UTF_8)
            } catch (e: FileNotFoundException) {
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

        line.substringAfterLast(":").toPlayerList().forEach {
            PlayerKindaButNotExactlyViewModel.add(it, cs)
        }

        if (Config[AUTO_SHOW_AND_HIDE] == "true") {
            cs.launch {
                WINDOW.isAlwaysOnTop = true
                delay(Config[AUTO_SHOW_AND_HIDE_DELAY].toLongOrNull() ?: 5000)
                WINDOW.isMinimized = true
            }
        }
    }

    private fun String.toPlayerList() = split(" ").filterNot { it.isBlank() }.distinct()
}
