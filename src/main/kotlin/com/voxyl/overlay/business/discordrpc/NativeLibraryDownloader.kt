package com.voxyl.overlay.business.discordrpc

import de.jcm.discordgamesdk.Core
import de.jcm.discordgamesdk.CreateParams
import java.io.File
import java.io.IOException
import java.net.URL
import java.nio.file.Files
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream


object NativeLibraryDownloader {
    @Throws(IOException::class)
    fun downloadDiscordLibrary(): File? {
        // Find out which name Discord's library has (.dll for Windows, .so for Linux)
        val name = "discord_game_sdk"
        val suffix: String
        val osName = System.getProperty("os.name").lowercase(Locale.ROOT)
        var arch = System.getProperty("os.arch").lowercase(Locale.ROOT)
        suffix = if (osName.contains("windows")) {
            ".dll"
        } else if (osName.contains("linux")) {
            ".so"
        } else if (osName.contains("mac os")) {
            ".dylib"
        } else {
            throw RuntimeException("cannot determine OS type: $osName")
        }

        /*
		Some systems report "amd64" (e.g. Windows and Linux), some "x86_64" (e.g. Mac OS).
		At this point we need the "x86_64" version, as this one is used in the ZIP.
		 */if (arch == "amd64") arch = "x86_64"

        // Path of Discord's library inside the ZIP
        val zipPath = "lib/$arch/$name$suffix"

        // Open the URL as a ZipInputStream
        val downloadUrl = URL("https://dl-game-sdk.discordapp.net/2.5.6/discord_game_sdk.zip")
        val zin = ZipInputStream(downloadUrl.openStream())

        // Search for the right file inside the ZIP
        var entry: ZipEntry
        while (zin.nextEntry.also { entry = it } != null) {
            if (entry.name == zipPath) {
                // Create a new temporary directory
                // We need to do this, because we may not change the filename on Windows
                val tempDir = File(System.getProperty("java.io.tmpdir"), "java-" + name + System.nanoTime())
                if (!tempDir.mkdir()) throw IOException("Cannot create temporary directory")
                tempDir.deleteOnExit()

                // Create a temporary file inside our directory (with a "normal" name)
                val temp = File(tempDir, name + suffix)
                temp.deleteOnExit()

                // Copy the file in the ZIP to our temporary file
                Files.copy(zin, temp.toPath())

                // We are done, so close the input stream
                zin.close()

                // Return our temporary file
                return temp
            }
            // next entry
            zin.closeEntry()
        }
        zin.close()
        // We couldn't find the library inside the ZIP
        return null
    }
}