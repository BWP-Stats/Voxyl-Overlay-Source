package com.voxyl.overlay.business.logfilereader

import com.voxyl.overlay.AppWindow
import com.voxyl.overlay.business.settings.config.*
import com.voxyl.overlay.business.stats.enitities.tags.FromGame
import com.voxyl.overlay.business.stats.enitities.tags.FromRegex
import com.voxyl.overlay.controllers.common.Aliases
import com.voxyl.overlay.controllers.playerstats.Entities
import com.voxyl.overlay.ui.settings.regex.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object LogFileInterpreter {
    private var customRegexProcessor = mutableListOf<CoroutineScope.(String) -> Unit>()

    fun reset() {
        for (_regex in Config[CustomRegex].split(REGEX_INTRA_SEPERATOR)) {
            if (_regex.isBlank()) return

            val (regexstr, action, matchIn, matchType) = _regex.split(REGEX_INTER_SEPERATOR)

            val regex = regexstr.toRegex()

            customRegexProcessor += processor@{ _line ->
                if (matchIn == REGEX_MATCH_IN_ONLY_CHAT && " [CHAT] ONLINE: " !in _line) return@processor

                val line = if (matchIn == REGEX_MATCH_IN_ONLY_CHAT) _line.substringAfter(" [CHAT] ONLINE: ") else _line

                val matches = (if (matchType == REGEX_MATCH_TYPE_FULLY_MATCHES) regex.matchEntire(line) else regex.find(line))?.groupValues

                if (action == REGEX_ACTION_ADD) {
                    for (i in 1 until (matches?.size ?: 1))
                        Entities.add(matches!![i], this, FromRegex)
                } else {
                    for (i in 1 until (matches?.size ?: 1))
                        Entities.remove(matches!![i])
                }
            }
        }
    }

    fun interpret(line: String, cs: CoroutineScope) {
        checkForBwpGameStart(line, cs)
        checkForHypixelGameStart(line, cs)
        checkForPlayerFinalKilled(line)
        checkForPlayerLeftBWP(line)
        checkForCustomMatches(line, cs)
        checkForNewNick(line)
    }

    private fun checkForBwpGameStart(line: String, cs: CoroutineScope) {
        if (" [CHAT] Players in this game: " !in line) return

        Entities.removeAll()

        line.substringAfterLast(":").toPlayerList().forEach {
            Entities.add(it, cs, FromGame)
        }

        autoShowAndHide(cs)
    }

    private fun checkForHypixelGameStart(line: String, cs: CoroutineScope) {
        if (" [CHAT] ONLINE: " !in line) return

        Entities.removeAll()

        line.substringAfterLast(":").toPlayerList().forEach {
            Entities.add(it, cs, FromGame)
        }

        autoShowAndHide(cs)
    }

    private fun checkForPlayerFinalKilled(line: String) {
        if (" [CHAT] " !in line) return
        if (!line.endsWith(" FINAL KILL!")) return

        Entities.remove(
            line.substringAfter("[CHAT] ").substringBefore(" ")
        )
    }

    private fun checkForPlayerLeftBWP(line: String) {
        if (" [CHAT] " !in line) return
        if (!line.endsWith(" has left the game!")) return

        Entities.remove(
            line.substringAfter("[CHAT] ").substringBefore(" ")
        )
    }

    private fun checkForCustomMatches(line: String, cs: CoroutineScope) {
        for (regexProcessor in customRegexProcessor) {
            cs.regexProcessor(line)
        }
    }

    private fun checkForNewNick(line: String) {
        if ("[CHAT] Your nick has been set to '" in line) {
            Aliases.addNick( line.split("'")[1] )
        }
    }

    private fun String.toPlayerList() = split(" ", ", ").filterNot { it.isBlank() }.distinct()

    private fun autoShowAndHide(cs: CoroutineScope) {
        if (Config[AutoShowAndHide] == "true") {
            cs.launch(Dispatchers.Default) {
                AppWindow.focusableWindowState = false
                AppWindow.isMinimized = false
                AppWindow.focusableWindowState = true
                delay(Config[AutoShowAndHideDelay].toLongOrNull() ?: 5000)
                AppWindow.isMinimized = true
            }
        }
    }
}
