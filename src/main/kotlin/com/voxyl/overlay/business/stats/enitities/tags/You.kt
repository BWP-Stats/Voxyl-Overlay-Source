package com.voxyl.overlay.business.stats.enitities.tags

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.voxyl.overlay.controllers.common.ui.am

object You : Tag {
    override val name: String = "account.png"

    override val icon: @Composable (Modifier) -> Unit = {
        Tag.TagIcon(this, it, Color(66, 126, 255).am)
    }

    override val desc: String = "You"
}
