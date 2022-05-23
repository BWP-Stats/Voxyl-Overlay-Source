package com.voxyl.overlay.business.networking.player.tags

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.voxyl.overlay.ui.theme.am

object Party : Tag {
    override val name: String = "account-group.png"

    override val icon: @Composable (Modifier) -> Unit = {
        Tag.TagIcon(this, it, Color(128, 61, 184).am)
    }

    override val desc: String = "My party"
}