package com.voxyl.overlay.ui.mainview

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.voxyl.overlay.middleman.PlayerKindaButNotExactlyViewModel
import com.voxyl.overlay.middleman.PlayerState
import com.voxyl.overlay.ui.common.elements.MyText
import com.voxyl.overlay.ui.common.elements.scrollbar
import com.voxyl.overlay.ui.common.util.requestFocusOnClick
import com.voxyl.overlay.ui.theme.MainWhite
import com.voxyl.overlay.ui.theme.am
import com.voxyl.overlay.ui.theme.tbsm
import kotlin.math.max
import kotlin.math.sqrt

@Composable
fun StatsHeader(statsToShow: State<List<String>>) = Column(
    Modifier.fillMaxWidth()
) {
    Row(
        modifier = Modifier
            .absoluteOffset(y = 64.tbsm.dp)
            .height(26.dp)
            .align(Alignment.CenterHorizontally)
            .fillMaxWidth(.95f)
            .requestFocusOnClick()
    ) {
        for (stat in statsToShow.value.toList()) {
            MyText(
                text = stat.substringAfterLast("."),
                modifier = Modifier.weight(cellWeight(stat)),
                fontSize = 13.sp,
                color = MainWhite
            )
        }
    }
}

@Composable
fun PlayerStats(statsToShow: State<List<String>>, lazyListState: LazyListState) {
    LazyColumn(
        state = lazyListState,
        modifier = Modifier.absoluteOffset(y = 90.tbsm.dp).fillMaxSize().scrollbar(lazyListState).requestFocusOnClick(),
        contentPadding = PaddingValues(bottom = 50.dp)
    ) {
        PlayerKindaButNotExactlyViewModel.players.forEach {
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
    statsToShow: State<List<String>>
) {
    Column(
        modifier = modifier.fillMaxWidth().height(36.dp)
    ) {
        Divider(
            color = MainWhite.copy(alpha = .313f).am,
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
            for (stat in statsToShow.value.toList()) {
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
    val text = if (statToShow == "name") player.name else player[statToShow] ?: if (player.isLoading) "..." else "ERR"
    MyText(
        text = text,
        modifier = modifier.weight(cellWeight(statToShow)).offset(y = 5.dp),
        fontSize = 15.sp,
        color = MainWhite
    )
}

private fun cellWeight(stat: String): Float {
    val stats = PlayerKindaButNotExactlyViewModel.players.map {
        it[stat] ?: ""
    }

    val max = stats.maxByOrNull { it.substringAfterLast(".").length } ?: stat

    return max(stat.getCellWeight(), sqrt(max.length.toFloat()))
}

private fun String.getCellWeight() = when (this) {
    "name" -> 5f
    else -> 2f
}
