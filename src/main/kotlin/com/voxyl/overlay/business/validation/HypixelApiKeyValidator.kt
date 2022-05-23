package com.voxyl.overlay.business.validation

import com.voxyl.overlay.business.networking.apis.ApiProvider
import com.voxyl.overlay.settings.config.Config
import retrofit2.HttpException

object HypixelApiKeyValidator {
    suspend fun validateApiKey(): Boolean? {
        if (Config["hypixel_api_key"].isNullOrEmpty()) {
            return false
        }

        return try {
            val response = ApiProvider
                .getHypixelApi()
                .getKeyInfo(Config["hypixel_api_key"]!!)

            if (response.get("success").asString == "true") {
                return true
            }

            return response.get("success").asString == "Invalid API key"
        } catch (e: HttpException) {
            return if (e.code() == 403) false else null
        } catch (e: Exception) {
            null
        }
    }
}