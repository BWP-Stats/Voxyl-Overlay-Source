package com.voxyl.overlay.ui.entitystats

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.voxyl.overlay.business.settings.config.CenterStats
import com.voxyl.overlay.business.settings.config.Config
import com.voxyl.overlay.controllers.common.ui.MainWhite
import com.voxyl.overlay.controllers.common.ui.tbsm
import com.voxyl.overlay.controllers.playerstats.StatsSort
import com.voxyl.overlay.controllers.playerstats.StatsToShow
import com.voxyl.overlay.ui.elements.VText
import com.voxyl.overlay.ui.elements.util.requestFocusOnClick
import com.voxyl.overlay.ui.entitystats.columns.Column
import com.voxyl.overlay.ui.entitystats.columns.Column.Companion.prettyName

@Composable
fun EntityStats() {
    EntityStatsViewHeader(StatsToShow.stats)
    EntityStatsView(StatsToShow.stats)
}

@Composable
fun EntityStatsViewHeader(statsToShow: List<String>) = Column(
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
            Header(stat)
        }
    }
}

@Composable
private fun RowScope.Header(dataString: String) = Row(
    modifier = Modifier.weight(headerWeight(dataString))
        .clickable {
            if (dataString != "tags") {
                StatsSort.by = dataString
            }
        },
    horizontalArrangement = if (Config[CenterStats] != "false") Arrangement.Center else Arrangement.Start
) {
    VText(
        text = dataString.prettyName(),
        fontSize = 15.5.sp,
        fontWeight = FontWeight.Medium,
    )
    if (StatsSort.by == dataString) {
        Icon(
            Icons.Filled.ArrowDropDown,
            contentDescription = null,
            tint = MainWhite,
            modifier = Modifier
                .rotate(if (StatsSort.ascending) 180f else 0f)
                .offset(y = if (StatsSort.ascending) 1.2.dp else -1.2.dp)
        )
    }
}

private fun headerWeight(dataString: String) = Column.getMetadataForDataString(dataString).cellWeight
