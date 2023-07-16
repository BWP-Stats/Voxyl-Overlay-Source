@file:Suppress("HasPlatformType", "JoinDeclarationAndAssignment")

package com.voxyl.overlay.ui.entitystats.stats

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import com.voxyl.overlay.business.stats.enitities.Entity
import com.voxyl.overlay.business.stats.enitities.types.Bot
import com.voxyl.overlay.business.utils.COLORED_ERROR_PLACEHOLDER
import com.voxyl.overlay.business.utils.DASH_STRING
import com.voxyl.overlay.business.utils.LOADING_STRING
import com.voxyl.overlay.ui.entitystats.stats.Statistic.Companion.selectableStat
import com.voxyl.overlay.ui.entitystats.stats.util.CellWeights
import com.voxyl.overlay.ui.entitystats.toAnnotatedString

class BwpKills(override val entity: Entity) : Statistic {
    private val kills: AnnotatedString

    init {
        kills = when {
            entity.isLoading -> LOADING_STRING

            entity.raw is Bot -> DASH_STRING

            else -> entity[dataString]?.toAnnotatedString()
                ?: COLORED_ERROR_PLACEHOLDER
        }

        CellWeights.put(dataString, entity, weight = kills.length * .85)
    }

    @Composable
    override fun RowScope.display(entity: Entity) = Statistic.DefaultStatCell(
        kills,
        Modifier
            .weight(cellWeight)
            .selectableStat(entity)
    )

    companion object : Statistic.Metadata {
        override val prettyName = "Killsᴮ"
        override val actualName = this::class.java.simpleName
        override val dataString = "bwp.kills"

        override val isSortable = true

        override val hasAdditionalSettings = false

        override val cellWeight: Float
            get() = CellWeights.get(dataString, default = 1f)
    }
}
