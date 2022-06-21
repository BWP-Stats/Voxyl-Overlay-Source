package com.voxyl.overlay.business.autoupdater

import com.voxyl.overlay.appinfo.AppInfo
import com.voxyl.overlay.business.autoupdater.apis.GitHubApiProvider
import com.voxyl.overlay.business.validation.popups.Confirmation
import com.voxyl.overlay.business.validation.popups.Error
import com.voxyl.overlay.business.validation.popups.Info
import com.voxyl.overlay.controllers.common.PopUpQueue
import io.github.aakira.napier.Napier
import kotlinx.coroutines.*
import java.io.File

object UpdateChecker {
    fun check(cs: CoroutineScope, manuallyChecked: Boolean = false) {
        cs.launch(Dispatchers.IO) {
            try {
                deleteInstallerDirectory()

                val latestRelease = GitHubApiProvider.getApi().getReleases().get(0).asJsonObject
                val tag = latestRelease.get("tag_name").asString.trim('v')

                val latestVersion = tag.split(".")
                val currentVersion = AppInfo.VERSION.split(".")

                if (currentVersion < latestVersion) {
                    queryUpdate(tag, cs)
                    return@launch
                } else if (manuallyChecked) {
                    PopUpQueue.add(Info("No new releases found (Current version ${AppInfo.VERSION})"))
                }
            } catch (e: IndexOutOfBoundsException) {
                PopUpQueue.add(Error("Error checking for updates; no releases found"))
                Napier.e("Failed to check for updates; no releases found? ${e.message}")
            } catch (e: Exception) {
                PopUpQueue.add(Error("Failed to check for updates"))
                Napier.e("Failed to check for updates; ${e.message}")
            }
        }
    }

    private fun deleteInstallerDirectory() {
        val installerDir = File("./installers")
        installerDir.deleteRecursively()
    }

    private fun queryUpdate(tag: String, cs: CoroutineScope) {
        PopUpQueue.add(Confirmation("New version available: v$tag. Update?", 15000) {
            AutoUpdater.installUpdate(tag, cs)
            PopUpQueue.Current.cancel()
        })
    }

    private operator fun List<String>.compareTo(latestVersion: List<String>): Int {
        var i = 0
        while (i < this.size && i < latestVersion.size) {
            val thisVersion = this[i].toInt()
            val latestVersion2 = latestVersion[i].toInt()

            if (thisVersion < latestVersion2) {
                return -1
            } else if (thisVersion > latestVersion2) {
                return 1
            }

            i++
        }

        return 0
    }
}