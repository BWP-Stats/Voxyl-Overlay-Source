package com.voxyl.overlay.business.playerfetching.player.tags

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.voxyl.overlay.controllers.common.ui.am

object Error : Tag {
    override val name: String = "account-question.png"

    override val icon: @Composable (Modifier) -> Unit = {
        Tag.TagIcon(this, it, Color(255, 85, 85).am)
    }

    override val desc: String = "Error, nicked maybe?"
}
