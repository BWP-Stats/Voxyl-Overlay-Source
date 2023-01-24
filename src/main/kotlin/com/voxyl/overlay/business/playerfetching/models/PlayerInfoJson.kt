package com.voxyl.overlay.business.playerfetching.models

import com.google.gson.JsonObject

@JvmInline
value class PlayerInfoJson(override val json: JsonObject) : JsonValueClass {
    override fun toString() = json.toString()
}
