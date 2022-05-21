package com.voxyl.overlay.dataslashbusiness.apis

import retrofit2.http.GET
import retrofit2.http.Path

interface MojangApi {
    @GET("{user}")
    suspend fun getUUID(@Path("user") name: String): String
}