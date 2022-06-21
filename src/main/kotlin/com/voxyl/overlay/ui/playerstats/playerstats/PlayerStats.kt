package com.voxyl.overlay.ui.playerstats.playerstats

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.voxyl.overlay.controllers.playerstats.StatsSort
import com.voxyl.overlay.controllers.playerstats.StatsToShow
import com.voxyl.overlay.business.settings.config.Config
import com.voxyl.overlay.business.settings.config.ConfigKeys.CenterStats
import com.voxyl.overlay.ui.elements.util.requestFocusOnClick
import com.voxyl.overlay.controllers.playerstats.StatsToShow.clean
import com.voxyl.overlay.controllers.common.ui.MainWhite
import com.voxyl.overlay.ui.elements.VText
import com.voxyl.overlay.controllers.common.ui.tbsm

@Composable
fun PlayerStats() {
    PlayerStatsViewHeader(StatsToShow._stats)
    PlayerStatsView(StatsToShow._stats)
}

@Composable
fun PlayerStatsViewHeader(statsToShow: SnapshotStateList<String>) = Column(
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
            Row(
                modifier = Modifier.weight(cellWeight(stat))
                    .clickable {
                        if (stat != "tags") {
                            StatsSort.by = stat
                        }
                    },
                horizontalArrangement = if (Config[CenterStats] != "false") Arrangement.Center else Arrangement.Start
            ) {
                VText(
                    text = stat.clean(),
                    fontSize = 15.5.sp,
                    fontWeight = FontWeight.Medium,
                )
                if (StatsSort.by == stat) {
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
        }
    }
}