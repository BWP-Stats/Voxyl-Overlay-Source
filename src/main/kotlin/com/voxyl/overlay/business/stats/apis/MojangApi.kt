package com.voxyl.overlay.business.stats.apis

import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface MojangApi {
    @GET("{user}")
    suspend fun getUUID(@Path("user") name: String): Response<JsonObject>
}
