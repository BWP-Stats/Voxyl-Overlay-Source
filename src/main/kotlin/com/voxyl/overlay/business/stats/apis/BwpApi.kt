package com.voxyl.overlay.business.stats.apis

import com.google.gson.JsonObject
import retrofit2.Response

interface BwpApi {
    suspend fun getPlayerInfo(uuid: String, apiKey: String): Response<JsonObject>
    suspend fun getOverallStats(uuid: String, apiKey: String): Response<JsonObject>
    suspend fun getGameStats(uuid: String, apiKey: String): Response<JsonObject>
    suspend fun getLevelLeaderboard(apiKey: String): Response<JsonObject>
    suspend fun getWWLeaderboard(apiKey: String): Response<JsonObject>
}
