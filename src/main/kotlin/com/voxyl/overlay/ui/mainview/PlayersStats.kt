package com.voxyl.overlay.ui.mainview

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.voxyl.overlay.middleman.PlayerKindaButNotExactlyViewModel
import com.voxyl.overlay.middleman.PlayerState
import com.voxyl.overlay.settings.config.Config
import com.voxyl.overlay.settings.config.ConfigKeys.ShowRankPrefix
import com.voxyl.overlay.settings.config.ConfigKeys.CenterStats
import com.voxyl.overlay.ui.common.elements.scrollbar
import com.voxyl.overlay.ui.common.util.requestFocusOnClick
import com.voxyl.overlay.ui.mainview.RankColors.coloredName
import com.voxyl.overlay.ui.mainview.RankColors.coloredRank
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
            Text(
                text = stat.substringAfterLast("."),
                modifier = Modifier.weight(cellWeight(stat)),
                fontSize = 13.sp,
                color = MainWhite,
                textAlign = if (Config[CenterStats].toBooleanStrictOrNull() != false) TextAlign.Center else null
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
            for (stat in statsToShow.value) {
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
    val text = getStat(statToShow, player)
    Text(
        text = text,
        modifier = modifier.weight(cellWeight(statToShow)).offset(y = 5.dp),
        fontSize = 15.sp,
        color = MainWhite,
        textAlign = if (Config[CenterStats].toBooleanStrictOrNull() != false) TextAlign.Center else null
    )
}

@Composable
private fun getStat(statToShow: String, player: PlayerState) = when {
    statToShow == "name" -> {
        if (player.player != null && Config[ShowRankPrefix].toBooleanStrictOrNull() == true) {
            coloredRank(player) + " ".toAnnotatedString() + coloredName(player)
        } else {
            coloredName(player)
        }
    }

    player[statToShow] != null -> player[statToShow]?.toAnnotatedString() ?: "ERR".toAnnotatedString()

    player.isLoading -> "...".toAnnotatedString()

    else -> "ERR".toAnnotatedString()
}

private fun String.toAnnotatedString(): AnnotatedString {
    return buildAnnotatedString { append(this@toAnnotatedString) }
}

internal fun cellWeight(stat: String): Float {
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
