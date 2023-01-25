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
import com.voxyl.overlay.controllers.playerstats.Entities
import com.voxyl.overlay.ui.entitystats.stats.Statistic.Companion.selectableStat
import java.lang.Float.max

class Tags(override val entity: Entity) : Statistic {
    override val prettyName = "Tags"
    override val actualName = "Tags"
    override val dataString = "tags"

    @Composable
    override fun RowScope.display(entity: Entity) = Row(
        modifier = Modifier
            .weight(cellWeight)
            .align(Alignment.CenterVertically)
            .selectableStat(entity),
        horizontalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        for (tag in entity.tags) {
            tag.icon(Modifier.size(24.dp))
        }
    }

    companion object {
        val cellWeight: Float
            get() {
                val tagLengths = Entities.entities.map {
                    "--".repeat(it.tags.size)
                }

                val max = tagLengths.maxByOrNull { it.length } ?: ""

                return max(1f, max.length.toFloat() / 3f)
            }
    }
}
