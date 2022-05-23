package com.voxyl.overlay.business.player.tags

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

object FromGame : Tag {
    override val name: String = ""
    override val icon: @Composable (Modifier) -> Unit = { }
    override val desc: String = "Player added from your game starting"
}