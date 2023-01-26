@file:Suppress("HasPlatformType", "JoinDeclarationAndAssignment")

package com.voxyl.overlay.ui.entitystats.stats

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import com.voxyl.overlay.business.statsfetching.enitities.Entity
import com.voxyl.overlay.business.statsfetching.enitities.types.Bot
import com.voxyl.overlay.ui.entitystats.stats.Statistic.Companion.selectableStat
import com.voxyl.overlay.ui.entitystats.stats.util.*

class BwpLevel(override val entity: Entity) : Statistic {
    private val level: AnnotatedString

    init {
        level = when {
            entity.isLoading -> LOADING_STRING

            entity.raw is Bot -> DASH_STRING

            else -> entity[dataString]?.let(::formatAndColorLevel)
                ?: COLORED_ERROR_PLACEHOLDER
        }

        CellWeights.put(dataString, entity, weight = level.length * .85)
    }

    @Composable
    override fun RowScope.display(entity: Entity) = Statistic.DefaultStatCell(
        level,
        Modifier
            .weight(cellWeight)
            .selectableStat(entity)
    )

    companion object : Statistic.Metadata {
        override val prettyName = "Levelᴮ"
        override val actualName = this::class.java.simpleName
        override val dataString = "bwp.level"

        override val isSortable = true

        override val hasAdditionalSettings = false

        override val cellWeight: Float
            get() = CellWeights.get(dataString, default = 1f)
    }
}