package com.voxyl.overlay.business.stats.enitities.tags

import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.voxyl.overlay.ui.elements.VTooltip

sealed interface Tag {
    val name: String
    val icon: @Composable (Modifier) -> Unit
    val desc: String

    companion object {
        @Composable fun TagIcon(tag: Tag, modifier: Modifier, color: Color) = VTooltip(tag.desc) {
            Icon(
                painter = painterResource("tags/${tag.name}"),
                contentDescription = tag.name,
                modifier = modifier,
                tint = color
            )
        }
    }
}
