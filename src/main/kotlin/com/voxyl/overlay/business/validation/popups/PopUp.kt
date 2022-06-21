package com.voxyl.overlay.business.validation.popups

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.voxyl.overlay.controllers.common.PopUpQueue

sealed class PopUp(val text: String, var duration: Long = 5000L, var cancelled: Boolean = false) {
    abstract val color: Color
    abstract val icon: @Composable (Modifier) -> Unit

    open val clickableIcon = Icons.Filled.Close
    open val onClick = { PopUpQueue.Current.cancel() }

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
