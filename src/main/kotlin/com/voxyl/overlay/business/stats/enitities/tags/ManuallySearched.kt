package com.voxyl.overlay.business.stats.enitities.tags

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.voxyl.overlay.controllers.common.ui.am

object ManuallySearched : Tag {
    override val name: String = "form-textbox.png"

    override val icon: @Composable (Modifier) -> Unit = {
        Tag.TagIcon(this, it, Color(181, 179, 51).am)
    }

    override val desc: String = "Manually added"
}
