package com.voxyl.overlay.business.statsfetching.apis

import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface HypixelApi {
    @GET("/player")
    suspend fun getStats(
        @Query("uuid") uuid: String,
        @Query("key") apiKey: String
    ): Response<JsonObject>

    @GET("/key")
    suspend fun getKeyInfo(
        @Query("key") apiKey: String
    ): Response<JsonObject>
}
