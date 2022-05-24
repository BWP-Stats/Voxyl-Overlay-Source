package com.voxyl.overlay.ui.playerstats.playerstats

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
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
import com.voxyl.overlay.kindasortasomewhatviewmodelsishiguessithinkidkwhatevericantbebotheredsmh.StatsSort
import com.voxyl.overlay.kindasortasomewhatviewmodelsishiguessithinkidkwhatevericantbebotheredsmh.StatsToShow
import com.voxyl.overlay.settings.config.Config
import com.voxyl.overlay.settings.config.ConfigKeys.CenterStats
import com.voxyl.overlay.ui.elements.util.requestFocusOnClick
import com.voxyl.overlay.kindasortasomewhatviewmodelsishiguessithinkidkwhatevericantbebotheredsmh.StatsToShow.clean
import com.voxyl.overlay.ui.theme.MainWhite
import com.voxyl.overlay.ui.theme.VText
import com.voxyl.overlay.ui.theme.tbsm

@Composable
fun PlayerStats() {
    val playerStatsLazyListState = rememberLazyListState()

    PlayerStatsViewHeader(StatsToShow._stats)
    PlayerStatsView(StatsToShow._stats, playerStatsLazyListState)
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