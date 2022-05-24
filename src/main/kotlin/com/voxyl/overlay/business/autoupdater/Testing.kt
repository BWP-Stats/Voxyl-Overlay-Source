package com.voxyl.overlay.business.autoupdater

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Streaming
import java.awt.Desktop
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import kotlin.system.exitProcess

interface GitHubAPI {
    @GET("/repos/BWP-Stats/Voxyl-Overlay/releases?token=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")
    suspend fun getReleases(): JsonArray

    @GET("/repos/BWP-Stats/Voxyl-Overlay/releases/{id}/assets?token=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")
    suspend fun getAssets(@Path("id") id: String): JsonArray
}

interface GitHubApi2 {
    @Streaming
    @GET("/BWP-Stats/Voxyl-Overlay/releases/download/{tag}/{asset}?token=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")
    suspend fun downloadAsset(@Path("tag") tag: String, @Path("asset") asset: String): Response<ResponseBody>
}

fun getGitHubApi(): GitHubAPI =
    Retrofit.Builder()
        .baseUrl("https://api.github.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(GitHubAPI::class.java)

fun getGitHubApi2(): GitHubApi2 =
    Retrofit.Builder()
        .baseUrl("https://github.com/")
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()
        .create(GitHubApi2::class.java)


suspend fun main() {
    val latestRelease = getGitHubApi().getReleases().get(0).asJsonObject
    val assetsId = latestRelease.get("id").asString
    val tag = latestRelease.get("tag_name").asString

//    val assets = getGitHubApi().getAssets(assetsId)
//    val browserDownloadLink = assets.get(0).asJsonObject.get("browser_download_url").asString

    val assetDownloadResponse = getGitHubApi2().downloadAsset(tag, "form-textbox.png")

    println(assetDownloadResponse)

    _openInstaller(File(saveFile(assetDownloadResponse.body(), System.getenv("APPDATA") + "\\.voverlay\\form-textbox.png")))
}

fun saveFile(body: ResponseBody?, pathWhereYouWantToSaveFile: String): String {
    if (body == null)
        return ""

    var input: InputStream? = null

    try {
        input = body.byteStream()
        //val file = File(getCacheDir(), "cacheFileAppeal.srl")
        val fos = FileOutputStream(pathWhereYouWantToSaveFile)
        fos.use { output ->
            val buffer = ByteArray(4 * 1024) // or other buffer size
            var read: Int
            while (input.read(buffer).also { read = it } != -1) {
                output.write(buffer, 0, read)
            }
            output.flush()
        }
        return pathWhereYouWantToSaveFile
    } finally {
        input?.close()
    }
}

fun _openInstaller(file: File) {
    if (Desktop.isDesktopSupported()) {
        try {
            Desktop.getDesktop().open(file)
            exitProcess(0)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}