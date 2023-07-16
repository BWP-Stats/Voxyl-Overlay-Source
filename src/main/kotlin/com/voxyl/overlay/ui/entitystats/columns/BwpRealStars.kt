@file:Suppress("HasPlatformType", "JoinDeclarationAndAssignment")

package com.voxyl.overlay.ui.entitystats.columns

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import com.voxyl.overlay.business.stats.enitities.Entity
import com.voxyl.overlay.business.stats.enitities.types.Bot
import com.voxyl.overlay.business.utils.COLORED_ERROR_PLACEHOLDER
import com.voxyl.overlay.business.utils.DASH_STRING
import com.voxyl.overlay.business.utils.LOADING_STRING
import com.voxyl.overlay.ui.entitystats.columns.Column.Companion.selectableStat
import com.voxyl.overlay.ui.entitystats.columns.util.*

class BwpRealStars(override val entity: Entity) : Column {
    private val stars: AnnotatedString

    init {
        stars = when {
            entity.isLoading -> LOADING_STRING

            entity.raw is Bot -> DASH_STRING

            else -> entity[dataString]?.let(::formatAndColorLevel)
                ?: COLORED_ERROR_PLACEHOLDER
        }

        CellWeights.put(dataString, entity, weight = stars.length * .85)
    }

    @Composable
    override fun RowScope.display(entity: Entity) = Column.DefaultStatCell(
        stars,
        Modifier
            .weight(cellWeight)
            .selectableStat(entity)
    )

    companion object : Column.Metadata {
        override val prettyName = "RealStars™"
        override val actualName = this::class.java.simpleName
        override val dataString = "bwp.realstars"

        override val isSortable = true

        override val hasAdditionalSettings = false

        override val cellWeight: Float
            get() = CellWeights.get(dataString, default = 1f)
    }
}
