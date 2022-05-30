package com.voxyl.overlay.business.logfilereader

import com.voxyl.overlay.Window
import com.voxyl.overlay.business.playerfetching.player.tags.FromGame
import com.voxyl.overlay.kindasortasomewhatviewmodelsishiguessithinkidkwhatevericantbebotheredsmh.PlayerKindaButNotExactlyViewModel
import com.voxyl.overlay.business.settings.config.Config
import com.voxyl.overlay.business.settings.config.ConfigKeys.AutoShowAndHideDelay
import com.voxyl.overlay.business.settings.config.ConfigKeys.AutoShowAndHide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object LogFileInterpreter {
    fun interpret(line: String, cs: CoroutineScope) {
        checkForBwpGameStart(line, cs)
        checkForHypixelGameStart(line, cs)
        checkForPlayerFinalKilled(line)
        checkForPlayerLeftBWP(line)
    }

    private fun checkForBwpGameStart(line: String, cs: CoroutineScope) {
        if (" [CHAT] Players in this game: " !in line) return

        PlayerKindaButNotExactlyViewModel.removeAll()

        line.substringAfterLast(":").toPlayerList().forEach {
            PlayerKindaButNotExactlyViewModel.add(it, cs, FromGame)
        }

        autoShowAndHide(cs)
    }

    private fun checkForHypixelGameStart(line: String, cs: CoroutineScope) {
        if (" [CHAT] ONLINE: " !in line) return

        PlayerKindaButNotExactlyViewModel.removeAll()

        line.substringAfterLast(":").toPlayerList().forEach {
            PlayerKindaButNotExactlyViewModel.add(it, cs, FromGame)
        }

        autoShowAndHide(cs)
    }

    private fun checkForGameEnd(line: String) {
        if ("          Winner - " !in line) return
        if ("       The winning team is " !in line) return

        PlayerKindaButNotExactlyViewModel.removeAll()
    }

    private fun checkForPlayerFinalKilled(line: String) {
        if (" [CHAT] " !in line) return
        if (!line.endsWith(" FINAL KILL!")) return

        println("|${line.substringAfter("[CHAT] ").substringBefore(" ")}| ")

        PlayerKindaButNotExactlyViewModel.remove(
            line.substringAfter("[CHAT] ").substringBefore(" ")
        )
    }

    private fun checkForPlayerLeftBWP(line: String) {
        if (" [CHAT] " !in line) return
        if (!line.endsWith(" has left the game!")) return

        println("|${line.substringAfter("[CHAT] ").substringBefore(" ")}| ")

        PlayerKindaButNotExactlyViewModel.remove(
            line.substringAfter("[CHAT] ").substringBefore(" ")
        )
    }

    private fun String.toPlayerList() = split(" ", ", ").filterNot { it.isBlank() }.distinct()

    private fun autoShowAndHide(cs: CoroutineScope) {
        if (Config[AutoShowAndHide] == "true") {
            cs.launch(Dispatchers.Default) {
                Window.focusableWindowState = false
                Window.isMinimized = false
                Window.focusableWindowState = true
                delay(Config[AutoShowAndHideDelay].toLongOrNull() ?: 5000)
                Window.isMinimized = true
            }
        }
    }
}