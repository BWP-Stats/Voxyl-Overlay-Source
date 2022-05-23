package com.voxyl.overlay.business.events

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

sealed class Event(val text: String, var duration: Long = 5000L, var cancelled: Boolean = false) {
    abstract val color: Color
    abstract val icon: @Composable (Modifier) -> Unit

    var tags = listOf<String>()

    fun withTags(vararg tags: String) = this.also {
        this.tags += tags.toList()
    }

    fun cancel() {
        cancelled = true
    }

    companion object {
        fun empty() = Info("Hello World!")
    }
}
