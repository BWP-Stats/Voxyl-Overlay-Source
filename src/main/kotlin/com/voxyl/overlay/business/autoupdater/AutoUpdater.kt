package com.voxyl.overlay.business.autoupdater

import com.voxyl.overlay.business.autoupdater.apis.GitHubApiProvider
import com.voxyl.overlay.business.settings.Settings
import com.voxyl.overlay.business.validation.popups.Error
import com.voxyl.overlay.business.validation.popups.Info
import com.voxyl.overlay.business.validation.popups.Warning
import com.voxyl.overlay.controllers.common.PopUpQueue
import io.github.aakira.napier.Napier
import kotlinx.coroutines.*
import okhttp3.ResponseBody
import java.awt.Desktop
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import kotlin.system.exitProcess

object AutoUpdater {
    fun installUpdate(tag: String, cs: CoroutineScope) {
        cs.launch(Dispatchers.IO) {
            try {
                val fileType = getFileType() ?: return@launch

                val assetDownloadResponse = GitHubApiProvider
                    .getDownloader()
                    .downloadAsset("v$tag", "VoxylOverlay-$tag.$fileType")

                val path = saveFile(assetDownloadResponse.body(), "./installers/Voxyl.Overlay-$tag.$fileType")

                Settings.backupToTemp()

                if (path.isNotBlank()) {
                    install(path, cs)
                }
            } catch (e: Exception) {
                PopUpQueue.add(Error("Failed to download installation file"))
                Napier.e("Failed to download installation file", e)
            }
        }
    }

    private fun getFileType() = when (OsCheck.getOs()) {
        OsType.Windows -> "msi"
//        OsType.MacOS -> "dmg"
//        OsType.Linux -> "deb"
        else -> {
            PopUpQueue.add(Warning("Please manually download the latest version, or make a support ticket in the discord"))
            null
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
                val buffer = ByteArray(4 * 1024)
                var read: Int
                while (input.read(buffer).also { read = it } != -1) {
                    output.write(buffer, 0, read)
                }
                output.flush()
            }
            return path
        } catch (e: IOException) {
            PopUpQueue.add(Error("Failed to save installation file"))
            Napier.e("Failed to save installation file", e)
            return ""
        } finally {
            input?.close()
        }
    }

    private fun install(filePath: String, cs: CoroutineScope) {
        Napier.i(filePath)

        try {
            cs.launch {
                Runtime.getRuntime().addShutdownHook(Thread {
                    Runtime.getRuntime().exec("cmd /c msiexec.exe /i \"${filePath}\" /passive > nul 2>&1")
                })

                delay(1350)
                exitProcess(0)
            }
        } catch (e: IOException) {
            PopUpQueue.add(Warning("Downloaded successfully, but failed to install. Please manually open the backup settings then install."))
        }
    }
}
