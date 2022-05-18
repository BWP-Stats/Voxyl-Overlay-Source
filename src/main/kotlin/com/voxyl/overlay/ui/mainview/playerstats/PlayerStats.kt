package com.voxyl.overlay.ui.mainview.playerstats

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.voxyl.overlay.settings.config.Config
import com.voxyl.overlay.settings.config.ConfigKeys.CenterStats
import com.voxyl.overlay.ui.common.util.requestFocusOnClick
import com.voxyl.overlay.ui.theme.MainWhite
import com.voxyl.overlay.ui.theme.VText
import com.voxyl.overlay.ui.theme.tbsm
import java.util.*

@Composable
fun PlayerStats() {
    val playerStatsLazyListState = rememberLazyListState()
    val statsToShow =
        remember { mutableStateListOf("tags", "bwp.level", "name", "bwp.wins", "bwp.kills", "bwp.finals") }

    PlayerStatsViewHeader(statsToShow)
    PlayerStatsView(statsToShow, playerStatsLazyListState)
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
                        if (stat == "tags") return@clickable
                        Sort.by = stat
                    },
                horizontalArrangement = if (Config[CenterStats].toBooleanStrictOrNull() != false) Arrangement.Center else Arrangement.Start
            ) {
                VText(
                    text = stat.substringAfterLast(".")
                        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
                    fontSize = 15.5.sp,
                    fontWeight = FontWeight.Medium,
                )
                if (Sort.by == stat) {
                    Icon(
                        Icons.Filled.ArrowDropDown,
                        contentDescription = null,
                        tint = MainWhite,
                        modifier = Modifier
                            .rotate(if (Sort.ascending) 180f else 0f)
                            .offset(y = if (Sort.ascending) 1.2.dp else -1.2.dp)
                    )
                }
            }
        }
    }
}