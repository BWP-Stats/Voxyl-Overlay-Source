package com.voxyl.overlay.ui.entitystats

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
import com.voxyl.overlay.business.settings.config.*
import com.voxyl.overlay.business.statsfetching.enitities.Entity
import com.voxyl.overlay.controllers.common.ui.MainWhite
import com.voxyl.overlay.controllers.common.ui.am
import com.voxyl.overlay.controllers.common.ui.tbsm
import com.voxyl.overlay.controllers.playerstats.Entities
import com.voxyl.overlay.controllers.playerstats.StatsSort
import com.voxyl.overlay.ui.elements.VText
import com.voxyl.overlay.ui.elements.util.requestFocusOnClick
import com.voxyl.overlay.ui.entitystats.colors.BwpRankColors
import com.voxyl.overlay.ui.entitystats.colors.HypixelRankColors
import com.voxyl.overlay.ui.entitystats.colors.LevelColors.coloredLevel
import com.voxyl.overlay.ui.entitystats.colors.getColoredErrorPlaceholder
import io.github.aakira.napier.Napier
import kotlin.math.max
import kotlin.math.sqrt

@Composable
fun EntityStatsView(statsToShow: SnapshotStateList<String>) {
    Column(
        modifier = Modifier
            .absoluteOffset(y = 90.tbsm.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        val rawEntities = Entities.entities.toList()
        var entity = StatsSort.sortEntitiesList(rawEntities)

        if (Config[PinYourselfToTop] != "false") {
            entity = entity.filter { it.name != Config[PlayerName] }.toMutableList()

            if (entity.size != rawEntities.size) {
                entity.add(0, rawEntities.first { it.name == Config[PlayerName] })
            }
        }

        for (player in entity) {
            EntitiesStatsBar(entity = player, statsToShow = statsToShow)
        }

        Spacer(modifier = Modifier.size(115.dp))
    }

    EntitiyContextMenu()
}

@Composable
fun EntitiesStatsBar(
    modifier: Modifier = Modifier,
    entity: Entity,
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
                .requestFocusOnClick(!entity.isLoading)
                .also {
                    selected = EntityContextMenuState.player == entity
                }
                .background(
                    if (selected) Color(200, 200, 200, 11).am else Color.Transparent
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (stat in statsToShow) {
                DisplayStat(entity = entity, statToShow = stat)
            }
        }
    }
}

@Composable
fun RowScope.DisplayStat(
    modifier: Modifier = Modifier,
    entity: Entity,
    statToShow: String,
) = when (statToShow) {
//    "tags" -> Statistic.get<Tags>(entity)()
    else -> StatCell(
        modifier = modifier,
        entity = entity,
        statToShow = statToShow
    )
}

@Composable
fun RowScope.StatCell(
    modifier: Modifier = Modifier,
    entity: Entity,
    statToShow: String,
) {
    Box(
        modifier = modifier
            .weight(cellWeight(statToShow))
            .selectable(entity),
    ) {
        VText(
            text = getStat(statToShow, entity),
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
    entity: Entity,
) = this
    .pointerMoveFilter(
        onMove = {
            EntityContextMenuState.player = entity
            true
        },
        onExit = {
            if (!EntityContextMenuState.show) {
                EntityContextMenuState.player = Entity.dummy
            }
            true
        }
    )
    .mouseClickable {
        if (buttons.isSecondaryPressed) {
            EntityContextMenuState.show = true
        }
    }

@Composable
private fun getStat(statToShow: String, player: Entity) = when {
    statToShow == "name" -> {
        figureOutWhichRankToUseAndIfToShowTheRankPrefixAndThenReturnTheOutputCorrespondingToTheMatchingCondition(player)
    }

    statToShow == "bwp.level" && player.raw != null -> {
        coloredLevel(player["bwp.level"] ?: "0")
    }

    statToShow == "bwp.realstars" && player.raw != null -> {
        coloredLevel(player["bwp.realstars"] ?: "0")
    }

    statToShow == "bedwars.level" && player.raw != null -> {
        coloredLevel(player["bedwars.level"] ?: "0")
    }

    statToShow == "tags" -> {
        player.tags.joinToString(", ").toAnnotatedString()
    }

    player[statToShow] != null -> player[statToShow]?.toAnnotatedString() ?: getColoredErrorPlaceholder(false)

    player.isLoading -> "...".toAnnotatedString()

    else -> getColoredErrorPlaceholder(false)
}

fun figureOutWhichRankToUseAndIfToShowTheRankPrefixAndThenReturnTheOutputCorrespondingToTheMatchingCondition(player: Entity): AnnotatedString {
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
        val tagLengths = Entities.entities.map {
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
        val stats = Entities.entities.map {
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
