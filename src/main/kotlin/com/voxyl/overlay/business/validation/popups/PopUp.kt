package com.voxyl.overlay.business.validation.popups

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

sealed class PopUp(val text: String, var duration: Long = 5000L, var cancelled: Boolean = false) {
    abstract val color: Color
    abstract val icon: @Composable (Modifier) -> Unit

    open val isConfirmation = false
    open val onConfirmationClick: () -> Unit = {}

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
