package com.voxyl.overlay.ui.elements

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.TooltipPlacement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.voxyl.overlay.controllers.common.ui.am

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VTooltip(
    desc: String,
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit
) {
    TooltipArea(
        modifier = modifier,
        tooltip = {
            Surface(
                color = Color(60, 60, 60, 50).am,
                shape = RoundedCornerShape(4.dp),
            ) {
                VText(
                    text = desc,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(10.dp)
                )
            }
        },
        delayMillis = 300,
        tooltipPlacement = TooltipPlacement.CursorPoint(
            alignment = Alignment.BottomEnd,
            offset = DpOffset(10.dp, 12.dp)
        )
    ) { icon() }
}
