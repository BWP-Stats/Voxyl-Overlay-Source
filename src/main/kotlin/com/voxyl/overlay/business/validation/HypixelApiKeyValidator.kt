package com.voxyl.overlay.business.validation

import com.voxyl.overlay.business.playerfetching.apis.ApiProvider
import com.voxyl.overlay.business.settings.config.Config

object HypixelApiKeyValidator {
    suspend fun isValid(): Boolean? {
        if (Config["hypixel_api_key"].isNullOrEmpty()) {
            return false
        }

        val response = ApiProvider
            .getHypixelApi()
            .getKeyInfo(Config["hypixel_api_key"]!!)

        if (response.isSuccessful) {
            return true
        }

        return if (response.code() == 403) false else null
    }
}