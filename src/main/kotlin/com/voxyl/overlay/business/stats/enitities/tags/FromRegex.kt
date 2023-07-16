package com.voxyl.overlay.business.stats.enitities.tags

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.voxyl.overlay.controllers.common.ui.am

object FromRegex : Tag {
    override val name: String = "regexmatch.png"

    override val icon: @Composable (Modifier) -> Unit = {
        Tag.TagIcon(this, it, Color(42, 213, 219).am)
    }

    override val desc: String = "Player was matched from your own custom regex"
}
