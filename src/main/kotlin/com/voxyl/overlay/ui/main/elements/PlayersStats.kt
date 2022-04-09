package com.voxyl.overlay.ui.main.elements

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.Divider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.voxyl.overlay.middleman.PlayerKindaButNotExactlyViewModel
import com.voxyl.overlay.middleman.PlayerState
import com.voxyl.overlay.ui.common.util.requestFocusOnClick
import com.voxyl.overlay.ui.theme.MainWhite
import com.voxyl.overlay.ui.theme.MainWhiteLessOpaque
import com.voxyl.overlay.viewelements.MyText

@Composable
fun StatsHeader(statsToShow: List<String>) = Column(
    Modifier.fillMaxWidth()
) {
    Row(
        modifier = Modifier
            .absoluteOffset(y = 64.dp)
            .height(26.dp)
            .align(Alignment.CenterHorizontally)
            .fillMaxWidth(.95f)
            .requestFocusOnClick()
    ) {
        for (stat in statsToShow.toList()) {
            MyText(
                text = stat.substring(stat.lastIndexOf(".") + 1),
                modifier = Modifier.weight(1f),
                fontSize = 13.sp,
                color = MainWhite
            )
        }
    }
}

@Composable
fun PlayerStats(statsToShow: List<String>, lazyListState: LazyListState) {
    LazyColumn(
        state = lazyListState,
        modifier = Modifier.absoluteOffset(y = 90.dp).fillMaxSize().scrollbar(lazyListState).requestFocusOnClick(),
        contentPadding = PaddingValues(bottom = 50.dp)
    ) {
        PlayerKindaButNotExactlyViewModel.getPlayersCopy().forEach {
            item {
                PlayersStatsBar(player = it, statsToShow = statsToShow)
            }
        }
    }
}

@Composable
fun PlayersStatsBar(
    modifier: Modifier = Modifier,
    player: PlayerState,
    statsToShow: List<String>
) {
    Column(
        modifier = modifier.fillMaxWidth().height(36.dp)
    ) {
            Divider(
                color = MainWhiteLessOpaque,
                modifier = Modifier
                    .fillMaxWidth(.9f)
                    .height(1.dp)
                    .align(Alignment.CenterHorizontally)
            )
        Row(
            modifier = Modifier
                .height(34.dp)
                .fillMaxWidth(.95f)
                .align(Alignment.CenterHorizontally)
                .requestFocusOnClick(!player.isLoading)
        ) {
           for (stat in statsToShow.toList()) {
               Cell(player = player, statToShow = stat)
           }
        }
    }
}

@Composable
fun RowScope.Cell(
    modifier: Modifier = Modifier,
    player: PlayerState,
    statToShow: String,
) {
    MyText(
        text = if (statToShow == "name") player.name else player[statToShow] ?: "n/a",
        modifier = modifier.weight(1f).offset(y = 5.dp),
        fontSize = 15.sp,
        color = MainWhite
    )
}

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

        if (needDrawScrollbar && firstVisibleElementIndex != null) {
            val elementHeight = (this.size.height / state.layoutInfo.totalItemsCount) - 4
            val scrollbarOffsetY = firstVisibleElementIndex * elementHeight
            val scrollbarHeight = state.layoutInfo.visibleItemsInfo.size * elementHeight

            drawRect(
                color = Color(130, 32, 229, 255),
                topLeft = Offset(this.size.width - width.toPx(), scrollbarOffsetY),
                size = Size(width.toPx(), scrollbarHeight),
                alpha = alpha
            )
        }
    }
}