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
import org.reflections.Reflections
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.full.primaryConstructor

interface Statistic {
    val entity: Entity

    @Composable
    fun RowScope.display(entity: Entity)

    @Composable
    operator fun invoke(rs: RowScope) = rs.display(entity)

    interface Metadata {
        val prettyName: String
        val actualName: String
        val dataString: String
        val cellWeight: Float
        val isSortable: Boolean
        val hasAdditionalSettings: Boolean
    }

    companion object {
        val implementations: Map<String, Class<out Statistic>>

        fun getStatisticForDataString(dataString: String, entity: Entity): Statistic {
            val clazz = implementations[dataString]!!
            val constructor = clazz.kotlin.primaryConstructor!!

            return constructor.call(entity)
        }

        fun getMetadataForDataString(dataString: String): Metadata {
            val clazz = implementations[dataString]!!
            val companion = clazz.kotlin.companionObjectInstance

            return (companion as Metadata)
        }

        fun String.prettyName(): String {
            return getMetadataForDataString(this).prettyName
        }

        @OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
        /*protected*/ fun Modifier.selectableStat(entity: Entity) = this
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
        /*protected*/ fun DefaultStatCell(
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

        init {
            val impls = Reflections(this::class.java.packageName)
                .getSubTypesOf(Statistic::class.java)

            val toPopulate = mutableMapOf<String, Class<out Statistic>>()

            implementations = impls.associateByTo(toPopulate) {
                (it.kotlin.companionObjectInstance as? Metadata)?.dataString
                    ?: throw IllegalStateException("Statistics implementation must have a Metadata companion object")
            }
        }
    }
}
