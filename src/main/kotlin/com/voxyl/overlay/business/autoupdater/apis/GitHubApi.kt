package com.voxyl.overlay.business.autoupdater.apis

import com.google.gson.JsonArray
import retrofit2.http.GET

interface GitHubApi {
    @GET("/repos/BWP-Stats/Voxyl-Overlay/releases")
    suspend fun getReleases(): JsonArray
}
