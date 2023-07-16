package com.voxyl.overlay.ui.entitystats.columns

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.voxyl.overlay.business.stats.enitities.Entity
import com.voxyl.overlay.business.stats.enitities.types.Bot
import com.voxyl.overlay.business.utils.COLORED_ERROR_PLACEHOLDER
import com.voxyl.overlay.business.utils.DASH_STRING
import com.voxyl.overlay.business.utils.LOADING_STRING
import com.voxyl.overlay.ui.elements.VTooltip
import com.voxyl.overlay.ui.entitystats.columns.Column.Companion.selectableStat
import com.voxyl.overlay.ui.entitystats.columns.util.CellWeights
import com.voxyl.overlay.ui.entitystats.toAnnotatedString

class BwpMainMode(override val entity: Entity) : Column {
    private var fullName: String? = null

    private val mainMode = when {
        entity.isLoading -> {
            LOADING_STRING
        }
        entity.raw is Bot -> {
            DASH_STRING
        }
        else -> {
            fullName = entity[dataString]
                ?.replace("([A-Z])".toRegex(), " $1")
                ?.capitalize()

            KEYS_MAP[entity[dataString]]?.toAnnotatedString()
        }
    } ?: COLORED_ERROR_PLACEHOLDER

    init {
        CellWeights.put(dataString, entity, weight = mainMode.length * .85)
    }

    @Composable
    override fun RowScope.display(entity: Entity) {
        val cell = @Composable {
            Column.DefaultStatCell(
                mainMode,
                Modifier
                    .weight(cellWeight)
                    .selectableStat(entity)
            )
        }

        return fullName?.let {
            VTooltip(fullName!!, Modifier.weight(cellWeight), cell)
        } ?: cell()
    }

    companion object : Column.Metadata {
        override val prettyName = "Mainᴮ"
        override val actualName = this::class.java.simpleName
        override val dataString = "bwp.main"

        override val isSortable = false

        override val hasAdditionalSettings = false

        override val cellWeight: Float
            get() = CellWeights.get(dataString, default = 1f)

        val KEYS_MAP = mapOf(
            "ladderFight" to "LDR",
            "bowFight"    to "BOW",
            "betaSumo"    to "BBS",
            "pearlFight"  to "PF",
            "miniwars"    to "MINW",
            "void"        to "VOID",
            "obstacle"    to "OBS",
            "sumo"        to "SUMO",
            "bedwars"     to "BW",
            "bridges"     to "BF",
            "resource"    to "RES",
            "ground"      to "GRND",
            "stickFight"  to "STK",
        )
    }
}
