package com.voxyl.overlay.business.playerfetching.player.tags

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.voxyl.overlay.controllers.common.ui.am

class LevelAndWWLB(lvlPos: String, wwPos: String) : Tag {
    override val name: String = "account-star.png"

    override val icon: @Composable (Modifier) -> Unit = {
        Tag.TagIcon(this, it, Color(201, 158, 16).am)
    }

    override val desc: String = "#$lvlPos on bwp levels lb, #$wwPos on w-wins"
}