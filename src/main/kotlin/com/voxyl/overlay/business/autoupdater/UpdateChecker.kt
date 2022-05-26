package com.voxyl.overlay.business.autoupdater

import com.voxyl.overlay.appinfo.AppInfo
import com.voxyl.overlay.business.autoupdater.OsType.*
import com.voxyl.overlay.business.autoupdater.apis.GitHubApiProvider
import com.voxyl.overlay.business.validation.popups.Confirmation
import com.voxyl.overlay.business.validation.popups.Warning
import com.voxyl.overlay.business.validation.popups.Error
import com.voxyl.overlay.business.validation.popups.Info
import com.voxyl.overlay.kindasortasomewhatviewmodelsishiguessithinkidkwhatevericantbebotheredsmh.PopUpQueue
import com.voxyl.overlay.settings.Settings
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import java.awt.Desktop
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path
import kotlin.system.exitProcess

object UpdateChecker {
    fun check(cs: CoroutineScope, manuallyChecked: Boolean = false) {
        cs.launch(Dispatchers.IO) {
            try {
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

                Files.walk(Path.of("./installers"))
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete)

            } catch (e: IndexOutOfBoundsException) {
                PopUpQueue.add(Error("Error checking for updates; no releases found"))
                Napier.e("Failed to check for updates; no releases found? ${e.message}")
            } catch (e: Exception) {
                PopUpQueue.add(Error("Failed to check for updates"))
                Napier.e("Failed to check for updates; ${e.message}")
            }
        }
    }

    private fun queryUpdate(tag: String, cs: CoroutineScope) {
        PopUpQueue.add(Confirmation("New version available: v$tag. Update?", 15000) {
            installUpdate(tag, cs)
            PopUpQueue.Current.cancel()
        })
    }

    private fun installUpdate(tag: String, cs: CoroutineScope) {
        cs.launch(Dispatchers.IO) {
            try {
                val fileType = when (OsCheck.getOs()) {
                    Windows -> "msi"
                    MacOS -> "dmg"
                    Linux, Other -> {
                        PopUpQueue.add(Warning("Please manually download the latest version, or make a support ticket in the discord"))
                        return@launch
                    }
                }

                val assetDownloadResponse =
                    GitHubApiProvider.getDownload().downloadAsset("v$tag", "VoxylOverlay-$tag.$fileType")

                val path = saveFile(
                    assetDownloadResponse.body(), "./installers/Voxyl.Overlay-$tag.$fileType"
                )

                if (path.isNotBlank()) {
                    openInstaller(File(path))
                }
            } catch (e: Exception) {
                PopUpQueue.add(Error("Failed to download installation file"))
                Napier.e("Failed to download installation file; ${e.message}")
            }
        }
    }

    private fun saveFile(body: ResponseBody?, path: String): String {
        if (body == null) {
            PopUpQueue.add(Error("Failed to download installation file"))
            Napier.e("Failed to download installation file; body was null")
            return ""
        }

        var input: InputStream? = null

        try {
            input = body.byteStream()
            FileOutputStream(
                File(path).apply {
                    parentFile.mkdirs()
                }
            ).use { output ->
                val buffer = ByteArray(4 * 1024) // or other buffer size
                var read: Int
                while (input.read(buffer).also { read = it } != -1) {
                    output.write(buffer, 0, read)
                }
                output.flush()
            }
            return path
        } catch (e: IOException) {
            PopUpQueue.add(Error("Failed to save installation file"))
            Napier.e("Failed to save installation file; ${e.message}")
            return ""
        } finally {
            input?.close()
        }
    }

    private fun openInstaller(file: File) {
        if (Desktop.isDesktopSupported()) {
            try {
                Settings.storeAll()
                Desktop.getDesktop().open(file)
                exitProcess(0)
            } catch (e: IOException) {
                PopUpQueue.add(Warning("Installed successfully, but failed to open installer. Please manually open the installer"))
            }
        }
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