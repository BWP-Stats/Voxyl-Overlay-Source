package com.voxyl.overlay.business.stats.enitities.tags

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

object EmptyTag : Tag {
    override val name: String = ""
    override val icon: @Composable (Modifier) -> Unit = { }
    override val desc: String = "Empty tag"
}
