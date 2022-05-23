package com.voxyl.overlay.business.networking.player.tags

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.TooltipPlacement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.voxyl.overlay.ui.theme.VText
import com.voxyl.overlay.ui.theme.am

sealed interface Tag {
    val name: String
    val icon: @Composable (Modifier) -> Unit
    val desc: String

    companion object {
        @OptIn(ExperimentalFoundationApi::class)
        @Composable
        fun TagIcon(
            tag: Tag,
            modifier: Modifier = Modifier,
            color: Color
        ) {
            TooltipArea(
                tooltip = {
                    Surface(
                        color = Color(60, 60, 60, 50).am,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        VText(
                            text = tag.desc,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                },
                delayMillis = 600,
                tooltipPlacement = TooltipPlacement.CursorPoint(
                    alignment = Alignment.BottomEnd,
                    offset = DpOffset(10.dp, 12.dp)
                )
            ) {
                Icon(
                    painter = painterResource("tags/${tag.name}"),
                    contentDescription = tag.name,
                    modifier = modifier,
                    tint = color
                )
            }
        }
    }
}