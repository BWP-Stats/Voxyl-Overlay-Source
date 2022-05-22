package com.voxyl.overlay.dataslashbusiness.player.tags

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.voxyl.overlay.ui.theme.am

object OverlayDev : Tag {
    override val name: String = "account.png"

    override val icon: @Composable (Modifier) -> Unit = {
        Tag.TagIcon(this, it, Color(13, 84, 26).am)
    }

    override val desc: String = "Overlay Developer"
}