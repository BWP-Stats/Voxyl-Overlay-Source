package com.voxyl.overlay.data.valueclasses

import com.google.gson.JsonObject

@JvmInline
value class WWinsLeaderboardJson(val json: JsonObject) {
    override fun toString() = json.toString()
}