package com.voxyl.overlay.business.stats.apis

import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

//HhtTKOr5nIvl8adDZMtaLAjsBhClrmvp
//c62d2b59-bf09-4517-a059-0925fac113d6
interface ActualBwpApi : BwpApi {
    @GET("/player/info/{uuid}")
    override suspend fun getPlayerInfo(
        @Path("uuid") uuid: String,
        @Query("api") apiKey: String
    ): Response<JsonObject>

    @GET("player/stats/overall/{uuid}")
    override suspend fun getOverallStats(
        @Path("uuid") uuid: String,
        @Query("api") apiKey: String
    ): Response<JsonObject>

    @GET("/player/stats/game/{uuid}")
    override suspend fun getGameStats(
        @Path("uuid") uuid: String,
        @Query("api") apiKey: String
    ): Response<JsonObject>

    @GET("/leaderboard/normal?type=level&num=100")
    override suspend fun getLevelLeaderboard(
        @Query("api") apiKey: String,
    ): Response<JsonObject>

    @GET("/leaderboard/normal?type=weightedwins&num=100")
    override suspend fun getWWLeaderboard(
        @Query("api") apiKey: String,
    ): Response<JsonObject>
}
