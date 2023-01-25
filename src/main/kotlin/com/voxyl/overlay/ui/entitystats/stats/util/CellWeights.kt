package com.voxyl.overlay.ui.entitystats.stats.util

import androidx.compose.runtime.mutableStateMapOf
import com.voxyl.overlay.business.statsfetching.enitities.Entity
import com.voxyl.overlay.controllers.playerstats.Entities
import com.voxyl.overlay.ui.entitystats.stats.Statistic
import java.util.*
import kotlin.math.max

object CellWeights {
    @PublishedApi
    internal val weights = mutableStateMapOf<Class<out Statistic>, DirtyFlagWeakHashMap<Entity, Float>>()

    @PublishedApi
    internal val states = mutableStateMapOf<Class<out Statistic>, Float>()

    init {
        Entities.addChangeListener { entity, type ->
            if (type == Entities.ChangeType.Remove) {
                weights.values.forEach { it.remove(entity) }
            }
        }
    }

    fun get(dataString: String, default: Number): Float {
        val clazz = Statistic.forDataString(dataString)

        if (weights[clazz]?.isDirty != false) {
            states[clazz] = max(default.toFloat(), (weights[clazz]?.maxOfOrNull { it.value } ?: 0f) / 3f)
        }
        return states[clazz]!!
    }

    inline fun <reified T : Statistic> get(default: Number): Float {
        if (weights[T::class.java]?.isDirty != false) {
            states[T::class.java] = max(default.toFloat(), (weights[T::class.java]?.maxOfOrNull { it.value } ?: 0f) / 3f)
        }
        return states[T::class.java]!!
    }

    inline fun <reified T : Statistic> put(entity: Entity, weight: Number) {
        weights.getOrPut(T::class.java, ::DirtyFlagWeakHashMap)[entity] = weight.toFloat()
    }

    class DirtyFlagWeakHashMap<K, V> : WeakHashMap<K, V>() {
        private var lastSize = 0

        var isDirty = true
           private set

        override fun put(key: K, value: V): V? {
            lastSize = size
            isDirty = true
            return super.put(key, value)
        }

        override fun get(key: K): V? {
            if (lastSize != size) {
                isDirty = true
            }
            return super.get(key)
        }
    }
}
