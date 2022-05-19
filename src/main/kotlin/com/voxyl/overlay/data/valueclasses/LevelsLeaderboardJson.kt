package com.voxyl.overlay.data.valueclasses

import com.google.gson.JsonObject

@JvmInline
value class LevelsLeaderboardJson(val json: JsonObject) {
    override fun toString() = json.toString()
}