package com.voxyl.overlay.ui.mainview.playerstats

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.voxyl.overlay.settings.config.Config
import com.voxyl.overlay.settings.config.ConfigKeys
import com.voxyl.overlay.ui.common.util.requestFocusOnClick
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
            VText(
                text = stat.substringAfterLast(".")
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
                modifier = Modifier.weight(cellWeight(stat)),
                fontSize = 15.5.sp,
                textAlign = if (Config[ConfigKeys.CenterStats].toBooleanStrictOrNull() != false) TextAlign.Center else null,
                fontWeight = FontWeight.Medium
            )
        }
    }
}