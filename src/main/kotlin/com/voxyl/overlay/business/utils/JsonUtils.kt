package com.voxyl.overlay.business.utils

import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import com.voxyl.overlay.business.stats.models.JsonValueClass

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

fun JsonObject?.getStr(key: String): String? {
    return this?.get(key)?.asString
}

fun JsonValueClass?.getStr(key: String): String? {
    return this?.json.getStr(key)
}

fun JsonObject?.getStrOrNull(key: String): String? {
    return (this?.get(key) as? JsonPrimitive)?.asString
}
