package com.voxyl.overlay.business.validation

import com.voxyl.overlay.business.NetworkingUtils
import com.voxyl.overlay.business.playerfetching.apis.ApiProvider
import com.voxyl.overlay.business.settings.config.Config

object BwpApiValidator {
    suspend fun isValid(): Boolean? {
        if (Config["bwp_api_key"].isNullOrEmpty()) {
            return false
        }

        val response = ApiProvider
            .getBWPApi()
            .getPlayerInfo("e21d44c5-c1fd-4119-b55c-5baced12fd6e", Config["bwp_api_key"]!!)

        if (response.isSuccessful) {
            return true
        }

        val errorReasonIsApiKey = NetworkingUtils
            .stringifyError(response)
            ?.get("reason")
            ?.asString
            ?.contains("api key", true)

        return if (errorReasonIsApiKey == true) false else null
    }
}