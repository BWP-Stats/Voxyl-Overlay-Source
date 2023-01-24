package com.voxyl.overlay.business.statsfetching.enitities.tags

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.voxyl.overlay.controllers.common.ui.am

class WWLB(wwPos: String) : Tag {
    override val name: String = "account-star.png"

    override val icon: @Composable (Modifier) -> Unit = {
        Tag.TagIcon(this, it, Color(122, 95, 6).am)
    }

    override val desc: String = "#$wwPos on bwp w-win lb"
}
