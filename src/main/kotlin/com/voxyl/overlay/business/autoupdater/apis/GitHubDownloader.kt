package com.voxyl.overlay.business.autoupdater.apis

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Streaming

interface GitHubDownloader {
    @Streaming
    @GET("/BWP-Stats/Voxyl-Overlay/releases/download/{tag}/{asset}")
    suspend fun downloadAsset(@Path("tag") tag: String, @Path("asset") asset: String): Response<ResponseBody>
}
