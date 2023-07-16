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
import kotlin.math.max

object UpdateChecker {
    fun check(cs: CoroutineScope, manuallyChecked: Boolean = false) = cs.launch(Dispatchers.IO) {
        try {
            deleteInstallerDirectory()

            val latestRelease = GitHubApiProvider.getApi().getReleases().get(0).asJsonObject
            val latestVersion = latestRelease.get("tag_name").asString.trim('v')

            if (compareVersions(latestVersion) > 0) {
                queryUpdate(latestVersion, cs)
            } else if (manuallyChecked) {
                PopUpQueue.add(Info("No new releases found (Current version ${AppInfo.VERSION})"))
            }
        } catch (e: IndexOutOfBoundsException) {
            PopUpQueue.add(Error("Error checking for updates; no releases found"))
            Napier.e("Failed to check for updates; no releases found?", e)
        } catch (e: Exception) {
            PopUpQueue.add(Error("Failed to check for updates"))
            Napier.e("Failed to check for updates", e)
        }
    }

    private fun deleteInstallerDirectory() {
        val installerDir = File("./installers")
        installerDir.deleteRecursively()
    }

    private fun queryUpdate(tag: String, cs: CoroutineScope) {
        PopUpQueue.add(Confirmation("New version available: v$tag. Update?", 15000) {
            AutoUpdater.installUpdate(tag, cs)
            PopUpQueue.endCurrent()

            cs.launch {
                PopUpQueue.add(Info("You'll just need to reopen the overlay once it's done", 3000))
            }
        })
    }

    private fun compareVersions(version1: String): Int {
        val v1 = version1.split(".")
        val v2 = AppInfo.VERSION.split(".")
        val maxLength = max(v1.size, v2.size)

        for (i in 0 until maxLength) {
            val ver1 = if (i < v1.size) v1[i].toInt() else 0
            val ver2 = if (i < v2.size) v2[i].toInt() else 0
            if (ver1 != ver2) {
                return ver1.compareTo(ver2)
            }
        }

        return 0
    }
}
