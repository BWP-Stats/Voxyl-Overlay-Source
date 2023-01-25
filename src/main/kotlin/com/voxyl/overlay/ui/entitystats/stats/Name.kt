package com.voxyl.overlay.ui.entitystats.stats

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.voxyl.overlay.business.settings.config.Config
import com.voxyl.overlay.business.settings.config.RankPrefix
import com.voxyl.overlay.business.settings.config.ShowRankPrefix
import com.voxyl.overlay.business.statsfetching.enitities.Entity
import com.voxyl.overlay.business.statsfetching.enitities.types.Bot
import com.voxyl.overlay.controllers.common.ui.am
import com.voxyl.overlay.ui.entitystats.colors.mcColors
import com.voxyl.overlay.ui.entitystats.stats.Statistic.Companion.DefaultStatCell
import com.voxyl.overlay.ui.entitystats.stats.Statistic.Companion.selectableStat
import com.voxyl.overlay.ui.entitystats.stats.util.getColorizedBwpName
import com.voxyl.overlay.ui.entitystats.stats.util.getColorizedBwpRank
import com.voxyl.overlay.ui.entitystats.stats.util.getColorizedHypixelName
import com.voxyl.overlay.ui.entitystats.stats.util.getColorizedHypixelRank
import com.voxyl.overlay.ui.entitystats.toAnnotatedString
import java.util.*
import kotlin.math.max
import kotlin.math.pow

class Name(override val entity: Entity) : Statistic {
    override val prettyName = "Name"
    override val actualName = "Name"
    override val dataString = "name"

    private val name: AnnotatedString

    init {
        name = getRank() + getName()
        names[entity] = name
    }

    @Composable
    override fun RowScope.display(entity: Entity) = DefaultStatCell(
        name,
        Modifier
            .weight(cellWeight)
            .selectableStat(entity)
    )

    companion object {
        private val names = WeakHashMap<Entity, AnnotatedString>()

        private var _cellWeight by mutableStateOf(4f)

        val cellWeight: Float
            get() {
                _cellWeight = max(4f, (names.maxOfOrNull { it.value.length }?.toFloat() ?: 0f).pow(.5f))
                return _cellWeight
            }
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
        withStyle(style = SpanStyle(color = mcColors["dark-gray"]!!.am)) {
            append(str)
        }
    }
}
