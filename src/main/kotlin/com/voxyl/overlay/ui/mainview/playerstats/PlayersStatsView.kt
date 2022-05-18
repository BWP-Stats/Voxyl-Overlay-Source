package com.voxyl.overlay.ui.mainview.playerstats

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.mouseClickable
import androidx.compose.material.Divider
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
import com.voxyl.overlay.ui.mainview.playerstats.colors.ErrorString
import com.voxyl.overlay.ui.mainview.playerstats.colors.LevelColors.coloredLevel
import com.voxyl.overlay.ui.mainview.playerstats.colors.RankColors.coloredName
import com.voxyl.overlay.ui.mainview.playerstats.colors.RankColors.coloredRank
import com.voxyl.overlay.ui.theme.MainWhite
import com.voxyl.overlay.ui.theme.VText
import com.voxyl.overlay.ui.theme.am
import com.voxyl.overlay.ui.theme.tbsm
import io.github.aakira.napier.Napier
import kotlin.math.max
import kotlin.math.sqrt

@Composable
fun PlayerStatsView(statsToShow: SnapshotStateList<String>, lazyListState: LazyListState) {
    LazyColumn(
        state = lazyListState,
        modifier = Modifier
            .absoluteOffset(y = 90.tbsm.dp)
            .fillMaxSize()
            .scrollbar(lazyListState)
            .requestFocusOnClick(),
        contentPadding = PaddingValues(bottom = 50.dp)
    ) {
        try {
            var players = PlayerKindaButNotExactlyViewModel.players.toList()

            players = sortPlayersList(players)

            items(items = players) {
                PlayersStatsBar(player = it, statsToShow = statsToShow)
            }
        } catch (e: Exception) {
            Napier.wtf(e) { "Error with iterating over the players in PlayersStarsView" }
            return@LazyColumn
        }
    }

    PlayerContextMenu()
}

fun sortPlayersList(players: List<PlayerState>): List<PlayerState> {
    return if (players.any { Sort.by != "name" }) {
        if (Sort.ascending) {
            players.sortedBy {
                if (it.error.isNotBlank()) Int.MAX_VALUE else it[Sort.by]?.toInt()
            }
        } else {
            players.sortedByDescending {
                if (it.error.isNotBlank()) 0 else it[Sort.by]?.toInt()
            }
        }
    } else {
        if (Sort.ascending) {
            players.sortedBy {
                if (it.error.isNotBlank()) "∐∐∐∐∐∐∐∐∐∐∐∐∐∐∐∐∐∐∐∐∐∐∐∐∐∐∐∐∐∐∐∐∐∐" else it[Sort.by]
            }
        } else {
            players.sortedByDescending {
                if (it.error.isNotBlank()) "                                  " else it[Sort.by]?.trimStart('_')
            }
        }
    }
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
                .align(Alignment.CenterHorizontally)
        )
        Row(
            modifier = Modifier
                .height(34.dp)
                .fillMaxWidth(.95f)
                .align(Alignment.CenterHorizontally)
                .requestFocusOnClick(!player.isLoading)
                .also {
                    selected = PlayerContextMenuState.player == player
                }
                .background(
                    if (selected) Color(200, 200, 200, 11).am else Color.Transparent
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (stat in statsToShow) {
                StatCell(player = player, statToShow = stat)
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
fun RowScope.StatCell(
    modifier: Modifier = Modifier,
    player: PlayerState,
    statToShow: String,
) {
    if (statToShow == "tags") {
        Row(
            modifier = modifier
                .weight(cellWeight(statToShow))
                .align(Alignment.CenterVertically)
                .requestFocusOnClick(),
            horizontalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            for (tag in player.tags) {
                tag.icon(Modifier.size(24.dp))
            }
        }
        return
    }

    val text = getStat(statToShow, player)
    Box(
        modifier = modifier
            .weight(cellWeight(statToShow))
            .pointerMoveFilter(
                onMove = {
                    PlayerContextMenuState.player = player
                    true
                },
                onExit = {
                    if (!PlayerContextMenuState.show) {
                        PlayerContextMenuState.player = PlayerState.empty
                    }
                    true
                }
            )
            .mouseClickable(
                onClick = {
                    if (buttons.isSecondaryPressed) {
                        PlayerContextMenuState.show = true
                    }
                }
            ),
    ) {
        VText(
            text = text,
            fontSize = 17.sp,
            textAlign = if (Config[CenterStats].toBooleanStrictOrNull() != false) TextAlign.Center else null,
            fontWeight = FontWeight.Medium,
            modifier = modifier
                .fillMaxSize()
                .offset(y = 5.dp)
        )
    }
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

    statToShow == "tags" -> {
        player.tags.joinToString(", ").toAnnotatedString()
    }

    player[statToShow] != null -> player[statToShow]?.toAnnotatedString() ?: ErrorString.get()

    player.isLoading -> "...".toAnnotatedString()

    else -> ErrorString.get()
}

fun playerIsNotNullSettingIsEnabledAndPlayerHasRank(player: PlayerState): Boolean {
    return player.player != null
            && Config[ShowRankPrefix].toBooleanStrictOrNull() != false
            && !player["bwp.role"].equals("none", true)
}

private fun String.toAnnotatedString(): AnnotatedString {
    return buildAnnotatedString { append(this@toAnnotatedString) }
}

fun cellWeight(stat: String) = when (stat) {
    "tags" -> calcTagCellWeight()
    else -> calcRegularCellWeight(stat)
}

fun calcTagCellWeight(): Float {
    val tagLengths = PlayerKindaButNotExactlyViewModel.players.map {
        "--".repeat(it.tags.size)
    }

    val max = tagLengths.maxByOrNull { it.length } ?: ""

    return max("tags".getCellWeight(), max.length.toFloat() / 3f)
}

fun calcRegularCellWeight(stat: String): Float {
    val stats = PlayerKindaButNotExactlyViewModel.players.map {
        it[stat] ?: ""
    }

    val max = stats.maxByOrNull { it.substringAfterLast(".").length } ?: ""

    return max(stat.getCellWeight(), sqrt(max.length.toFloat()))
}

private fun String.getCellWeight() = when (this) {
    "name" -> 5f
    "tags" -> 1f
    else -> 2f
}
