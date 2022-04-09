package com.voxyl.overlay.data.dto

import com.google.gson.JsonObject

sealed interface PlayerJson {
    val json: JsonObject
}