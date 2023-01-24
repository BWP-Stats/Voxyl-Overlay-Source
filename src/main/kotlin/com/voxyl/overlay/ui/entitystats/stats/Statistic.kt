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
import java.lang.RuntimeException

interface Statistic {
    fun get(entity: Entity): @Composable RowScope.() -> Unit
}
