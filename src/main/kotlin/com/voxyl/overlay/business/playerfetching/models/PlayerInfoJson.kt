package com.voxyl.overlay.business.playerfetching.models

import com.google.gson.JsonObject

@JvmInline
value class PlayerInfoJson(val json: JsonObject) {
    override fun toString() = json.toString()
}