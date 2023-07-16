package com.voxyl.overlay.business.stats.models

import com.google.gson.JsonObject

@JvmInline
value class LevelsLeaderboardJson(override val json: JsonObject) : JsonValueClass {
    override fun toString() = json.toString()
}
