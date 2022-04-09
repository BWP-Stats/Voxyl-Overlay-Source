package com.voxyl.overlay.data.apis

import retrofit2.http.GET
import retrofit2.http.Query

interface UUIDApi {
    @GET("/user")
    suspend fun getUUID(@Query("name") name: String): String
}