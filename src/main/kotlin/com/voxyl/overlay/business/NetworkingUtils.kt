package com.voxyl.overlay.business

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import retrofit2.Response

object NetworkingUtils {
    fun stringifyError(response: Response<*>): JsonObject? {
        if (response.isSuccessful) {
            throw IllegalStateException("Response was successful; no error body expected")
        }

        if (response.errorBody() == null) {
            return null
        }

        val gson = Gson()
        val type = object : TypeToken<JsonObject>() {}.type
        return gson.fromJson(response.errorBody()!!.charStream(), type)
    }
}