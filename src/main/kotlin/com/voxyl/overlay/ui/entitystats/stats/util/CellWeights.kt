//package com.voxyl.overlay.ui.entitystats.stats.util
//
//import androidx.compose.runtime.mutableStateMapOf
//import androidx.compose.ui.text.AnnotatedString
//import com.voxyl.overlay.business.statsfetching.enitities.Entity
//import com.voxyl.overlay.ui.entitystats.stats.Statistic
//import java.util.WeakHashMap
//
//object CellWeights {
//    @PublishedApi
//    internal val strings = mutableMapOf<Class<out Statistic>, WeakHashMap<Entity, String>>()
//
//    @PublishedApi
//    internal val weights = mutableStateMapOf<Class<out Statistic>, Float>()
//
//    inline fun <reified T : Statistic> get(entity: Entity): Float {
////        weights[T::class.java] =
//    }
//
//    inline fun <reified T : Statistic> put(entity: Entity, str: String) {
//        strings[T::class.java]!![entity] = str
//    }
//}
