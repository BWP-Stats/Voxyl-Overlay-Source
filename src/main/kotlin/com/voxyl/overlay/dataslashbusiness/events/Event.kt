package com.voxyl.overlay.dataslashbusiness.events

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

sealed class Event(val text: String, var duration: Long = 5000L, var cancelled: Boolean = false) {
    abstract val color: Color
    abstract val icon: @Composable (Modifier) -> Unit

    fun cancel() {
        cancelled = true
    }

    companion object {
        fun empty() = Info("Hello World!")
    }
}

