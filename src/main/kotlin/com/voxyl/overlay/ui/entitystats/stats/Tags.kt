package com.voxyl.overlay.ui.entitystats.stats

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.voxyl.overlay.business.statsfetching.enitities.Entity
import com.voxyl.overlay.ui.entitystats.cellWeight
import com.voxyl.overlay.ui.entitystats.stats.Statistics.Companion.selectableStat

@Composable
fun RowScope.TagCell(entity: Entity) = Row(
    modifier = Modifier
        .weight(cellWeight("tags"))
        .align(Alignment.CenterVertically)
        .selectableStat(entity),
    horizontalArrangement = Arrangement.spacedBy(1.dp)
) {
    for (tag in entity.tags) {
        tag.icon(Modifier.size(24.dp))
    }
}

//object Tags : Statistic {
//    override fun get(entity: Entity): @Composable RowScope.() -> Unit =
//        { TagCell(entity) }
//
//    @Composable
//    private fun RowScope.TagCell(entity: Entity) =
//        Row(
//            modifier = Modifier
//                .weight(cellWeight("tags"))
//                .align(Alignment.CenterVertically)
//                .selectableStat(entity),
//            horizontalArrangement = Arrangement.spacedBy(1.dp)
//        ) {
//            for (tag in entity.tags) {
//                tag.icon(Modifier.size(24.dp))
//            }
//        }
//}
