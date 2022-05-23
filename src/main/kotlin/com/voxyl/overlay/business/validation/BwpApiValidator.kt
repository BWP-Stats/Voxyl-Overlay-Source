package com.voxyl.overlay.business.validation

import com.voxyl.overlay.business.networking.apis.ApiProvider
import com.voxyl.overlay.settings.config.Config

object BwpApiValidator {
    suspend fun validateApiKey(): Boolean? {
        if (Config["bwp_api_key"].isNullOrEmpty()) {
            return false
        }

        return try {
            val response = ApiProvider
                .getBWPApi()
                .getPlayerInfo("e21d44c5-c1fd-4119-b55c-5baced12fd6e", Config["bwp_api_key"]!!)

            if (response.get("success").asString == "true") {
                return true
            }

            return response.get("reason").asString == "Unable to use API key!"
        } catch (e: Exception) {
            null
        }
    }
}