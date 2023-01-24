package com.voxyl.overlay.business

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import retrofit2.Response

object NetworkingUtils {
    fun stringifyError(response: Response<*>): JsonObject? {
        if (response.errorBody() == null) {
            return null
        }

        return Gson().fromJson(response.errorBody()!!.charStream(), JsonObject::class.java)
    }

    fun formattedError(response: Response<*>): String {
        return "HTTP ${response.code()}, ${stringifyError(response)}"
    }
}
