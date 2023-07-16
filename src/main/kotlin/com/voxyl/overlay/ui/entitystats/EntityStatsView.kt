package com.voxyl.overlay.ui.entitystats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import com.voxyl.overlay.business.settings.config.Config
import com.voxyl.overlay.business.settings.config.PinYourselfToTop
import com.voxyl.overlay.business.settings.config.PlayerName
import com.voxyl.overlay.business.stats.enitities.Entity
import com.voxyl.overlay.controllers.common.ui.MainWhite
import com.voxyl.overlay.controllers.common.ui.am
import com.voxyl.overlay.controllers.common.ui.tbsm
import com.voxyl.overlay.controllers.playerstats.Entities
import com.voxyl.overlay.controllers.playerstats.StatsSort
import com.voxyl.overlay.ui.elements.util.requestFocusOnClick
import com.voxyl.overlay.ui.entitystats.stats.Statistic

@Composable
fun EntityStatsView(dataStrings: List<String>) {
    Column(
        modifier = Modifier
            .absoluteOffset(y = 90.tbsm.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        val rawEntities = Entities.entities.toList()
        var entities = StatsSort.sortEntitiesList(rawEntities)

        if (Config[PinYourselfToTop] != "false") {
            entities = entities.filter { it.name != Config[PlayerName] }.toMutableList()

            if (entities.size != rawEntities.size) {
                entities.add(0, rawEntities.first { it.name == Config[PlayerName] })
            }
        }

        for (player in entities) {
            EntitiesStatsBar(entity = player, dataStrings = dataStrings)
        }

        Spacer(modifier = Modifier.size(115.dp))
    }

    EntitiyContextMenu()
}

@Composable
fun EntitiesStatsBar(
    modifier: Modifier = Modifier,
    entity: Entity,
    dataStrings: List<String>
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
            for (dataString in dataStrings) {
                DisplayStat(entity = entity, dataString = dataString)
            }
        }
    }
}

@Composable
fun RowScope.DisplayStat(
    entity: Entity,
    dataString: String,
) = Statistic.getStatisticForDataString(dataString, entity)(rs = this)

fun String.toAnnotatedString(): AnnotatedString {
    return buildAnnotatedString { append(this@toAnnotatedString) }
}
