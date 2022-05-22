package com.voxyl.overlay.dataslashbusiness.player.tags

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.voxyl.overlay.ui.theme.am

object FromGame : Tag {
    override val name: String = ""
    override val icon: @Composable (Modifier) -> Unit = { }
    override val desc: String = "Player added from your game starting"
}