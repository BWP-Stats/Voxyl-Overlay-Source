package com.voxyl.overlay.ui.entitystats.stats

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.mouseClickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.isSecondaryPressed
import androidx.compose.ui.input.pointer.pointerMoveFilter
import com.voxyl.overlay.business.statsfetching.enitities.Entity
import com.voxyl.overlay.ui.entitystats.EntityContextMenuState

enum class Statistics(private val display: @Composable RowScope.(Entity) -> Unit) {

    Tags({ TagCell(it) });

    operator fun RowScope.invoke(entity: Entity) {
        display(entity)
    }

    companion object {
        @JvmStatic
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
    }
}