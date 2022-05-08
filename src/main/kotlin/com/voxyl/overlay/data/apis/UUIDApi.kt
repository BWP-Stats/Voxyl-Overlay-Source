package com.voxyl.overlay.data.apis

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UUIDApi {
    @GET("{user}")
    suspend fun getUUID(@Path("user") name: String): String
}