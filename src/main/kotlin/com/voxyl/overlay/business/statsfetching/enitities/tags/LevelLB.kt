package com.voxyl.overlay.business.statsfetching.enitities.tags

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.voxyl.overlay.controllers.common.ui.am

class LevelLB(lvlPos: String) : Tag {
    override val name: String = "account-star.png"

    override val icon: @Composable (Modifier) -> Unit = {
        Tag.TagIcon(this, it, Color(140, 140, 140).am)
    }

    override val desc: String = "#$lvlPos on bwp levels lb"
}
