package com.voxyl.overlay.dataslashbusiness.player.tags

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.voxyl.overlay.ui.theme.am

object FromChat : Tag {
    override val name: String = "chat.png"

    override val icon: @Composable (Modifier) -> Unit = {
        Tag.TagIcon(this, it, Color(43, 176, 194).am)
    }

    override val desc: String = "Said your name in chat"
}