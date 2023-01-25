package com.voxyl.overlay.ui.entitystats.stats

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.voxyl.overlay.business.statsfetching.enitities.Entity
import com.voxyl.overlay.controllers.playerstats.Entities
import com.voxyl.overlay.ui.entitystats.stats.Statistic.Companion.selectableStat
import com.voxyl.overlay.ui.entitystats.stats.util.CellWeights
import java.lang.Float.max
import java.util.*

class Tags(override val entity: Entity) : Statistic {
    override val prettyName = "Tags"
    override val actualName = "Tags"
    override val dataString = "tags"

    init {
        CellWeights.put<Tags>(entity, weight = entity.tags.size * 2.5)
    }

    @Composable
    override fun RowScope.display(entity: Entity) = Row(
        modifier = Modifier
            .weight(CellWeights.get<Tags>(default = 2f))
            .align(Alignment.CenterVertically)
            .selectableStat(entity),
        horizontalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        for (tag in entity.tags) {
            tag.icon(Modifier.size(24.dp))
        }
    }
}
