package com.voxyl.overlay.business.logfilereader

import com.voxyl.overlay.business.events.Error
import com.voxyl.overlay.kindasortasomewhatviewmodelsishiguessithinkidkwhatevericantbebotheredsmh.PopupQueue
import com.voxyl.overlay.settings.config.Config
import com.voxyl.overlay.settings.config.ConfigKeys.LogFilePath
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
                FileInputStream(it).bufferedReader(Charsets.UTF_8).also {
                    PopupQueue.filter("LogFileError")
                }
            } catch (e: FileNotFoundException) {
                null
            }
        } ?: return@launch Unit.also {
            PopupQueue.add(
                Error(
                    "Error starting log file reader: Log file path may be invalid or inaccessible.",
                    10000
                ).withTags("LogFileError")
            )
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
                    LogFileInterpreter.interpret(line, cs)
                }
            }
        }
    }

    private fun skipToEndOfFile(reader: BufferedReader) = reader.skip(Long.MAX_VALUE)
}
