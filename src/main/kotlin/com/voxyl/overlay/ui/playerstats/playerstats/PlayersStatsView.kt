package com.voxyl.overlay.ui.playerstats.playerstats

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
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
import com.voxyl.overlay.business.networking.player.PlayerState
import com.voxyl.overlay.kindasortasomewhatviewmodelsishiguessithinkidkwhatevericantbebotheredsmh.PlayerKindaButNotExactlyViewModel
import com.voxyl.overlay.kindasortasomewhatviewmodelsishiguessithinkidkwhatevericantbebotheredsmh.StatsSort
import com.voxyl.overlay.settings.config.Config
import com.voxyl.overlay.settings.config.ConfigKeys.*
import com.voxyl.overlay.ui.elements.util.requestFocusOnClick
import com.voxyl.overlay.ui.playerstats.playerstats.colors.BwpRankColors
import com.voxyl.overlay.ui.playerstats.playerstats.colors.ErrorString
import com.voxyl.overlay.ui.playerstats.playerstats.colors.HypixelRankColors
import com.voxyl.overlay.ui.playerstats.playerstats.colors.LevelColors.coloredLevel
import com.voxyl.overlay.ui.theme.MainWhite
import com.voxyl.overlay.ui.elements.VText
import com.voxyl.overlay.ui.theme.am
import com.voxyl.overlay.ui.theme.tbsm
import io.github.aakira.napier.Napier
import kotlin.math.max
import kotlin.math.sqrt

@Composable
fun PlayerStatsView(statsToShow: SnapshotStateList<String>) {
    Column(
        modifier = Modifier
            .absoluteOffset(y = 90.tbsm.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        val rawPlayers = PlayerKindaButNotExactlyViewModel.players.toList()
        var players = StatsSort.sortPlayersList(rawPlayers)

        if (Config[PinYourselfToTop] != "false") {
            players = players.filter { it.name != Config[PlayerName] }.toMutableList()

            if (players.size != rawPlayers.size) {
                players.add(0, rawPlayers.first { it.name == Config[PlayerName] })
            }
        }

        for (player in players) {
            PlayersStatsBar(player = player, statsToShow = statsToShow)
        }

        Spacer(modifier = Modifier.size(115.dp))
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
                DisplayStat(player = player, statToShow = stat)
            }
        }
    }
}

@Composable
fun RowScope.DisplayStat(
    modifier: Modifier = Modifier,
    player: PlayerState,
    statToShow: String,
) {
    if (statToShow == "tags") {
        TagCell(
            modifier = modifier,
            player = player
        )
    } else {
        StatCell(
            modifier = modifier,
            player = player,
            statToShow = statToShow
        )
    }
}

@Composable
fun RowScope.TagCell(
    modifier: Modifier = Modifier,
    player: PlayerState
) {
    Row(
        modifier = modifier
            .weight(cellWeight("tags"))
            .align(Alignment.CenterVertically)
            .selectable(player),
        horizontalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        for (tag in player.tags) {
            tag.icon(Modifier.size(24.dp))
        }
    }
}

@Composable
fun RowScope.StatCell(
    modifier: Modifier = Modifier,
    player: PlayerState,
    statToShow: String,
) {
    val text = getStat(statToShow, player)
    Box(
        modifier = modifier
            .weight(cellWeight(statToShow))
            .selectable(player),
    ) {
        VText(
            text = text,
            fontSize = 17.sp,
            textAlign = if (Config[CenterStats] != "false") TextAlign.Center else null,
            fontWeight = FontWeight.Medium,
            modifier = modifier
                .fillMaxSize()
                .offset(y = 5.dp)
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
private fun Modifier.selectable(
    player: PlayerState,
) = this
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
    .mouseClickable {
        if (buttons.isSecondaryPressed) {
            PlayerContextMenuState.show = true
        }
    }

@Composable
private fun getStat(statToShow: String, player: PlayerState) = when {
    statToShow == "name" -> {
        figureOutWhichRankToUseAndIfToShowTheRankPrefixAndThenReturnTheOutputCorrespondingToTheMatchingCondition(player)
    }

    statToShow == "bwp.level" && player.player != null -> {
        coloredLevel(player["bwp.level"] ?: "0")
    }

    statToShow == "bwp.realstars" && player.player != null -> {
        coloredLevel(player["bwp.realstars"] ?: "0")
    }

    statToShow == "bedwars.level" && player.player != null -> {
        coloredLevel(player["bedwars.level"] ?: "0")
    }

    statToShow == "tags" -> {
        player.tags.joinToString(", ").toAnnotatedString()
    }

    player[statToShow] != null -> player[statToShow]?.toAnnotatedString() ?: ErrorString.get()

    player.isLoading -> "...".toAnnotatedString()

    else -> ErrorString.get()
}

fun figureOutWhichRankToUseAndIfToShowTheRankPrefixAndThenReturnTheOutputCorrespondingToTheMatchingCondition(player: PlayerState): AnnotatedString {
    return if (Config[ShowRankPrefix] == "false") {
        if (Config[RankPrefix] == "bwp") {
            BwpRankColors.coloredName(player)
        } else {
            HypixelRankColors.coloredName(player)
        }
    } else {
        if (Config[RankPrefix] == "bwp") {
            BwpRankColors.coloredRank(player) + BwpRankColors.coloredName(player)
        } else {
            HypixelRankColors.coloredRank(player) + HypixelRankColors.coloredName(player)
        }
    }
}

private fun String.toAnnotatedString(): AnnotatedString {
    return buildAnnotatedString { append(this@toAnnotatedString) }
}

fun cellWeight(stat: String) = when (stat) {
    "tags" -> calcTagCellWeight()
    else -> calcRegularCellWeight(stat)
}

fun calcTagCellWeight(): Float {
    return try {
        val tagLengths = PlayerKindaButNotExactlyViewModel.players.map {
            "--".repeat(it.tags.size)
        }

        val max = tagLengths.maxByOrNull { it.length } ?: ""

        max("tags".getCellWeight(), max.length.toFloat() / 3f)
    } catch (e: Exception) {
        Napier.wtf(e) { "Error with calculating the tag cell weight" }
        "tags".getCellWeight()
    }
}

fun calcRegularCellWeight(stat: String): Float {
    return try {
        val stats = PlayerKindaButNotExactlyViewModel.players.map {
            it[stat] ?: ""
        }

        val max = stats.maxByOrNull { it.substringAfterLast(".").length } ?: ""

        max(stat.getCellWeight(), sqrt(max.length.toFloat()))
    } catch (e: Exception) {
        Napier.wtf(e) { "Error with calculating the regular cell weight" }
        stat.getCellWeight()
    }
}

private fun String.getCellWeight() = when (this) {
    "name" -> 4f
    "tags" -> 1f
    else -> 2f
}
