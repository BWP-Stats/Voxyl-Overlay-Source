package com.voxyl.overlay.data.dto

import com.google.gson.JsonObject

class OverallStatsJson(override val json: JsonObject) : PlayerJson {
    override fun toString() = json.toString()
    override fun equals(other: Any?) = json == other
    override fun hashCode() = json.hashCode()
}