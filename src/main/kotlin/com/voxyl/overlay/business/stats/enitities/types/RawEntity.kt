package com.voxyl.overlay.business.stats.enitities.types

abstract class RawEntity(val name: String) {
    protected val data = mutableMapOf<String, String>()

    operator fun get(key: String): String? {
        return data[key]
    }

    override fun equals(other: Any?): Boolean {
        return name.equals(other as? String, true)
    }

    override fun hashCode(): Int {
        return name.lowercase().hashCode()
    }
}
