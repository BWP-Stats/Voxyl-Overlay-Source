package com.voxyl.overlay.data.apis

import com.google.gson.JsonObject
import com.voxyl.overlay.settings.config.Config
import com.voxyl.overlay.settings.config.ConfigKeys.HypixelApiKey
import retrofit2.http.GET
import retrofit2.http.Query

interface HypixelApi {
    @GET("/player")
    suspend fun getStats(
        @Query("uuid") uuid: String,
        @Query("key") apiKey: String
    ): JsonObject
}