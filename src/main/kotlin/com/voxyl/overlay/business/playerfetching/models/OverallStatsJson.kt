package com.voxyl.overlay.business.playerfetching.models

import com.google.gson.JsonObject

@JvmInline
value class OverallStatsJson(val json: JsonObject) {
    override fun toString() = json.toString()
}