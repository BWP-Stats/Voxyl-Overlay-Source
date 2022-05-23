package com.voxyl.overlay.ui.elements

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.voxyl.overlay.ui.theme.MainColor
import com.voxyl.overlay.ui.theme.am
import com.voxyl.overlay.ui.theme.tbsm

@Composable
fun Modifier.scrollbar(
    state: LazyListState,
    width: Dp = 8.dp
): Modifier {

    var previousOffset by remember { mutableStateOf(state.firstVisibleItemScrollOffset) }
    val offset = state.firstVisibleItemScrollOffset

    val mightBeScrolling = offset != previousOffset
    val allElementsVisible = state.layoutInfo.visibleItemsInfo.size == state.layoutInfo.totalItemsCount

    val targetAlpha = if (mightBeScrolling) 1f else if (allElementsVisible) 0f else .15f
    val duration = if (mightBeScrolling) 30 else if (allElementsVisible) 3000 else 500

    val alpha by animateFloatAsState(
        targetValue = targetAlpha,
        animationSpec = tween(durationMillis = duration)
    )

    previousOffset = state.firstVisibleItemScrollOffset

    return drawWithContent {
        drawContent()

        val firstVisibleElementIndex = state.layoutInfo.visibleItemsInfo.firstOrNull()?.index
        val needDrawScrollbar = mightBeScrolling || alpha > 0.0f

        val size = this@drawWithContent.size

        if (needDrawScrollbar && firstVisibleElementIndex != null) {
            val elementHeight = (size.height - 60.tbsm) / state.layoutInfo.totalItemsCount
            val scrollbarOffsetY = firstVisibleElementIndex * elementHeight
            val scrollbarHeight = state.layoutInfo.visibleItemsInfo.size * elementHeight

            drawRect(
                color = MainColor.value,
                topLeft = Offset(size.width - width.toPx(), scrollbarOffsetY),
                size = Size(width.toPx(), scrollbarHeight),
                alpha = alpha
            )
        }
    }
}