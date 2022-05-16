@file:OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)

package com.voxyl.overlay.ui.mainview.playerstats

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.mouseClickable
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.isSecondaryPressed
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.voxyl.overlay.data.player.PlayerState
import com.voxyl.overlay.middleman.PlayerKindaButNotExactlyViewModel
import com.voxyl.overlay.settings.config.Config
import com.voxyl.overlay.settings.config.ConfigKeys.CenterStats
import com.voxyl.overlay.settings.config.ConfigKeys.ShowRankPrefix
import com.voxyl.overlay.ui.common.elements.scrollbar
import com.voxyl.overlay.ui.common.util.requestFocusOnClick
import com.voxyl.overlay.ui.mainview.playerstats.LevelColors.coloredLevel
import com.voxyl.overlay.ui.mainview.playerstats.RankColors.coloredName
import com.voxyl.overlay.ui.mainview.playerstats.RankColors.coloredRank
import com.voxyl.overlay.ui.theme.MainWhite
import com.voxyl.overlay.ui.theme.am
import com.voxyl.overlay.ui.theme.tbsm
import java.util.*
import kotlin.math.max
import kotlin.math.sqrt

@Composable
fun StatsHeader(statsToShow: SnapshotStateList<String>) = Column(
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
        for (stat in statsToShow.toList()) {
            Text(
                text = stat.substringAfterLast(".")
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
                modifier = Modifier.weight(cellWeight(stat)),
                fontSize = 15.5.sp,
                color = MainWhite,
                textAlign = if (Config[CenterStats].toBooleanStrictOrNull() != false) TextAlign.Center else null,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun PlayerStats(statsToShow: SnapshotStateList<String>, lazyListState: LazyListState) {
    LazyColumn(
        state = lazyListState,
        modifier = Modifier
            .absoluteOffset(y = 90.tbsm.dp)
            .fillMaxSize()
            .scrollbar(lazyListState)
            .requestFocusOnClick(),
        contentPadding = PaddingValues(bottom = 50.dp)
    ) {
        PlayerKindaButNotExactlyViewModel.players.forEach {
            item {
                PlayersStatsBar(player = it, statsToShow = statsToShow)
            }
        }
    }

    PlayerContextMenu()
}

@Composable
fun PlayersStatsBar(
    modifier: Modifier = Modifier,
    player: PlayerState,
    statsToShow: SnapshotStateList<String>
) {
    var selected by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(36.dp)
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
                .pointerMoveFilter(
                    onEnter = {
                        selected = true
                        true
                    },
                    onExit = {
                        selected = false
                        true
                    }
                )
                .also {
                    if (PlayerContextMenuState.show && PlayerContextMenuState.player == player) {
                        selected = true
                    }
                }
                .background(
                    if (selected) Color(200, 200, 200, 20).am else Color.Transparent
                )
        ) {
            for (stat in statsToShow) {
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
        fontSize = 17.sp,
        color = MainWhite,
        textAlign = if (Config[CenterStats].toBooleanStrictOrNull() != false) TextAlign.Center else null,
        fontWeight = FontWeight.Medium,
        modifier = modifier
            .weight(cellWeight(statToShow))
            .offset(y = 5.dp)
            .mouseClickable(
                onClick = {
                    if (buttons.isSecondaryPressed) {
                        PlayerContextMenuState.show = true
                        PlayerContextMenuState.player = player
                    }
                }
            ),
    )
}

@Composable
private fun getStat(statToShow: String, player: PlayerState) = when {
    statToShow == "name" -> {
        if (playerIsNotNullSettingIsEnabledAndPlayerHasRank(player)) {
            coloredRank(player) + " ".toAnnotatedString() + coloredName(player)
        } else {
            coloredName(player)
        }
    }

    statToShow == "bwp.level" && player.player != null -> {
        coloredLevel(player["bwp.level"] ?: "0")
    }

    player[statToShow] != null -> player[statToShow]?.toAnnotatedString() ?: "ERR".toAnnotatedString()

    player.isLoading -> "...".toAnnotatedString()

    else -> "ERR".toAnnotatedString()
}

fun playerIsNotNullSettingIsEnabledAndPlayerHasRank(player: PlayerState): Boolean {
    return player.player != null
            && Config[ShowRankPrefix].toBooleanStrictOrNull() != false
            && !player["bwp.role"].equals("none", true)
}

private fun String.toAnnotatedString(): AnnotatedString {
    return buildAnnotatedString { append(this@toAnnotatedString) }
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
