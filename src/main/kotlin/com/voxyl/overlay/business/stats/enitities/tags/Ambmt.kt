package com.voxyl.overlay.business.stats.enitities.tags

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.voxyl.overlay.controllers.common.ui.am

object Ambmt : Tag {
    override val name: String = "trash-can.png"

    override val icon: @Composable (Modifier) -> Unit = {
        Tag.TagIcon(this, it, Color(115, 115, 115).am)
    }

    override val desc: String = "Ambmt"
}
