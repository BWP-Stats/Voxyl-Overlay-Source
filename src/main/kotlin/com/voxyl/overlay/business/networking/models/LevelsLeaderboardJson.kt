package com.voxyl.overlay.business.networking.models

import com.google.gson.JsonObject

@JvmInline
value class LevelsLeaderboardJson(val json: JsonObject) {
    override fun toString() = json.toString()
}