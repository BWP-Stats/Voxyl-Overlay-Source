package com.voxyl.overlay.business.playerfetching.player.tags

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.voxyl.overlay.ui.theme.am

object You : Tag {
    override val name: String = "account.png"

    override val icon: @Composable (Modifier) -> Unit = {
        Tag.TagIcon(this, it, Color(66, 126, 255).am)
    }

    override val desc: String = "You"
}