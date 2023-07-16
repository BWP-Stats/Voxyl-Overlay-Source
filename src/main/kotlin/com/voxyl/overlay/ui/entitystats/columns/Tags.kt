@file:Suppress("HasPlatformType")

package com.voxyl.overlay.ui.entitystats.columns

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.voxyl.overlay.business.stats.enitities.Entity
import com.voxyl.overlay.ui.entitystats.columns.Column.Companion.selectableStat
import com.voxyl.overlay.ui.entitystats.columns.util.CellWeights

class Tags(override val entity: Entity) : Column {
    init {
        CellWeights.put(dataString, entity, weight = entity.tags.size * 1.625)
    }

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

    companion object : Column.Metadata {
        override val prettyName = "Tags"
        override val actualName = this::class.java.simpleName
        override val dataString = "tags"

        override val isSortable = false

        override val hasAdditionalSettings = false

        override val cellWeight: Float
            get() = CellWeights.get(dataString, default = .5f)
    }
}
