package com.voxyl.overlay.business.stats.apis

import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface BackupBwpApi : BwpApi {
    @GET("/info")
    override suspend fun getPlayerInfo(
        @Query("uuid") uuid: String,
        @Query("") apiKey: String
    ): Response<JsonObject>

    @GET("/overall")
    override suspend fun getOverallStats(
        @Query("uuid") uuid: String,
        @Query("") apiKey: String
    ): Response<JsonObject>

    @GET("/game")
    override suspend fun getGameStats(
        @Query("uuid") uuid: String,
        @Query("") apiKey: String
    ): Response<JsonObject>

    @GET("/level")
    override suspend fun getLevelLeaderboard(
        apiKey: String,
    ): Response<JsonObject>

    @GET("/ww")
    override suspend fun getWWLeaderboard(
        apiKey: String,
    ): Response<JsonObject>
}
