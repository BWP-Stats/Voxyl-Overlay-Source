package com.voxyl.overlay.controllers.playerstats

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import com.voxyl.overlay.business.statsfetching.enitities.EntityFactory
import com.voxyl.overlay.business.statsfetching.enitities.Entity
import com.voxyl.overlay.business.statsfetching.enitities.ResponseStatus
import com.voxyl.overlay.business.statsfetching.enitities.TagGenerator
import com.voxyl.overlay.business.statsfetching.enitities.types.RawEntity
import com.voxyl.overlay.business.statsfetching.enitities.tags.Error
import com.voxyl.overlay.business.statsfetching.enitities.tags.Tag
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

object Entities {
    private var _entities = mutableStateListOf<Entity>()

    val entities: List<Entity>
        get() = _entities

    private var jobs = mutableMapOf<String, Job>()

    fun add(name: String, cs: CoroutineScope, vararg tags: Tag) {
        try {
            val tags2 = (listOf(*tags) + TagGenerator.generatePreTags(name))
                .distinctBy { tag -> tag.javaClass }
                .toMutableStateList()

            jobs[name] = EntityFactory.create(name).onEach {
                _entities += when (it) {
                    is ResponseStatus.Loaded -> {
                        _entities.remove(name)

                        Entity(
                            name = (it.data as RawEntity)["name"] ?: name,
                            raw = it.data,
                            tags = tags2
                        ).also { ps ->
                            try {
                                ps.tags += TagGenerator.generatePostTags(ps).distinctBy { tag -> tag.javaClass }
                            } catch (e: Exception) {
                                Napier.wtf(e) { "Failed to generate post tags" }
                            }
                        }
                    }

                    is ResponseStatus.Loading -> {
                        _entities.remove(name)

                        Entity(
                            name = name,
                            isLoading = true,
                            tags = tags2
                        )
                    }

                    is ResponseStatus.Error -> {
                        _entities.remove(name)

                        Entity(
                            name = name,
                            error = it.message ?: "An unexpected error has occurred",
                            tags = (tags2 + Error).distinctBy { tag -> tag.javaClass }.toMutableStateList()
                        )
                    }
                }
            }.launchIn(cs)
        } catch (e: Exception) {
            jobs[name]?.cancel()
            _entities.remove(name)
            Napier.wtf(e) { "Failed to add player $name (in PVM)" }
        }
    }

    private fun SnapshotStateList<Entity>.remove(name: String) {
        _entities.remove(Entity(name))
    }

    fun refreshAll(cs: CoroutineScope) {
        val entities = _entities.map {
            it.name to it.tags.toList()
        }

        removeAll()

        entities.forEach {
            add(it.first, cs, *it.second.toTypedArray())
        }
    }

    fun remove(name: String) {
        jobs[name]?.cancel()
        jobs.remove(name)
        _entities.remove(name)
    }

    fun removeAll() {
        jobs.forEach { it.value.cancel() }
        jobs.clear()
        _entities.clear()
    }
}
