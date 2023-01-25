package com.voxyl.overlay.ui.entitystats.stats

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.mouseClickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.isSecondaryPressed
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.voxyl.overlay.business.settings.config.CenterStats
import com.voxyl.overlay.business.settings.config.Config
import com.voxyl.overlay.business.statsfetching.enitities.Entity
import com.voxyl.overlay.ui.elements.VText
import com.voxyl.overlay.ui.entitystats.EntityContextMenuState

interface Statistic {
    val prettyName: String
    val actualName: String
    val dataString: String

    val entity: Entity

    @Composable
    fun RowScope.display(entity: Entity)

    @Composable
    operator fun invoke(rs: RowScope) = rs.display(entity)

    companion object {
        fun forDataString(dataString: String): Class<out Statistic> {
            val pascalCaseName = "\\.[a-zA-Z]".toRegex().replace(dataString) {
                it.value.replace(".","").uppercase()
            }.capitalize()

            return Class.forName(Statistic::class.java.packageName + '.' + pascalCaseName) as Class<out Statistic>
        }

        inline fun <reified T> get(entity: Entity): Statistic {
            return T::class.java.constructors[0].newInstance(entity) as Statistic
        }

        @OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
        fun Modifier.selectableStat(entity: Entity) = this
            .pointerMoveFilter(
                onMove = {
                    EntityContextMenuState.player = entity
                    true
                },
                onExit = {
                    if (!EntityContextMenuState.show) {
                        EntityContextMenuState.player = Entity.dummy
                    }
                    true
                }
            )
            .mouseClickable {
                if (buttons.isSecondaryPressed) {
                    EntityContextMenuState.show = true
                }
            }

        @Composable
        fun DefaultStatCell(
            text: AnnotatedString,
            modifier: Modifier,
        ) = Box(modifier) {
            VText(
                text = text,
                fontSize = 17.sp,
                textAlign = if (Config[CenterStats] != "false") TextAlign.Center else null,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .fillMaxSize()
                    .offset(y = 5.dp)
            )
        }
    }
}
