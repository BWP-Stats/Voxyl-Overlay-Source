@file:Suppress("HasPlatformType")

package com.voxyl.overlay.ui.entitystats.columns

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.voxyl.overlay.business.settings.config.Config
import com.voxyl.overlay.business.settings.config.RankPrefix
import com.voxyl.overlay.business.settings.config.ShowRankPrefix
import com.voxyl.overlay.business.stats.enitities.Entity
import com.voxyl.overlay.business.stats.enitities.types.Bot
import com.voxyl.overlay.controllers.common.ui.am
import com.voxyl.overlay.ui.entitystats.columns.Column.Companion.DefaultStatCell
import com.voxyl.overlay.ui.entitystats.columns.Column.Companion.selectableStat
import com.voxyl.overlay.ui.entitystats.columns.util.*
import com.voxyl.overlay.ui.entitystats.toAnnotatedString

class Name(override val entity: Entity) : Column {
    private val name: AnnotatedString

    init {
        name = getRank() + getName()
        CellWeights.put(dataString, entity, weight = name.length * .675)
    }

    @Composable
    override fun RowScope.display(entity: Entity) = DefaultStatCell(
        name,
        Modifier
            .weight(cellWeight)
            .selectableStat(entity)
    )

    companion object : Column.Metadata {
        override val prettyName = "Name"
        override val actualName = this::class.java.simpleName
        override val dataString = "name"

        override val isSortable = true

        override val hasAdditionalSettings = true

        override val cellWeight: Float
            get() = CellWeights.get(dataString, default = 2f)
    }

    private fun getName(): AnnotatedString {
        return when {
            entity.raw is Bot -> getBotName()

            Config[RankPrefix] == "bwp" -> getColorizedBwpName(entity)

            else -> getColorizedHypixelName(entity)
        }
    }

    private fun getRank(): AnnotatedString {
        return if (Config[ShowRankPrefix] != "false") {
            when {
                entity.raw is Bot -> getBotRank()

                Config[RankPrefix] == "bwp" -> getColorizedBwpRank(entity)

                else -> getColorizedHypixelRank(entity)
            }
        } else "".toAnnotatedString()
    }

    private fun getBotName() = darkGrayAnnotatedString(entity.name)

    private fun getBotRank() = darkGrayAnnotatedString("[Bot] ")

    private fun darkGrayAnnotatedString(str: String) = buildAnnotatedString {
        withStyle(style = SpanStyle(color = Color(0xFF888888).am)) {
            append(str)
        }
    }
}
