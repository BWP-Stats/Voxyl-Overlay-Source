package com.voxyl.overlay.business

import com.google.gson.JsonObject
import com.voxyl.overlay.business.playerfetching.models.JsonValueClass

inline fun <T> JsonObject?.ifExists(key: String, callback: () -> T): T? {
    return if (key in this) callback() else null
}

operator fun JsonObject?.contains(key: String): Boolean {
    return this?.get(key) != null
}

operator fun JsonObject?.get(vararg keys: String): JsonObject? {
    return keys.fold(this) { acc, key ->
        acc?.getAsJsonObject(key) ?: return null
    }
}

operator fun JsonValueClass?.get(vararg keys: String): JsonObject? {
    return this?.json.get(*keys)
}

fun JsonValueClass?.getStr(key: String): String? {
    return this?.json.getStr(key)
}


fun JsonObject?.getStr(key: String): String? {
    return this?.get(key)?.asString
}
