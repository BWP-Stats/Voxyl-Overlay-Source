package com.voxyl.overlay.business.networking.apis

import retrofit2.http.GET
import retrofit2.http.Path

interface MojangApi {
    @GET("{user}")
    suspend fun getUUID(@Path("user") name: String): String
}