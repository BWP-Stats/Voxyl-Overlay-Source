package com.voxyl.overlay.business.playerfetching.apis

import com.google.gson.JsonObject
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

//HhtTKOr5nIvl8adDZMtaLAjsBhClrmvp
//c62d2b59-bf09-4517-a059-0925fac113d6
interface BWPApi {
    @GET("/player/info/{uuid}")
    suspend fun getPlayerInfo(@Path("uuid") uuid: String, @Query("api") apiKey: String): JsonObject

    @GET("player/stats/overall/{uuid}")
    suspend fun getOverallStats(@Path("uuid") uuid: String, @Query("api") apiKey: String): JsonObject

    @GET("/player/stats/game/{uuid}")
    suspend fun getGameStats(@Path("uuid") uuid: String, @Query("api") apiKey: String): JsonObject

    @GET("/leaderboard/normal")
    suspend fun getLevelLeaderboard(
        @Query("api") apiKey: String,
        @Query("type") type: String = "level",
        @Query("num") number: String = "100"
    ): JsonObject

    @GET("/leaderboard/normal")
    suspend fun getWWLeaderboard(
        @Query("api") apiKey: String,
        @Query("type") type: String = "weightedwins",
        @Query("num") number: String = "100"
    ): JsonObject
}