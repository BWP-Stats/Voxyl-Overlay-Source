package com.voxyl.overlay.ui.entitystats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.voxyl.overlay.business.stats.enitities.Entity
import com.voxyl.overlay.controllers.common.ui.MainWhite
import com.voxyl.overlay.controllers.common.ui.alphaMultiplier
import com.voxyl.overlay.controllers.playerstats.Entities
import com.voxyl.overlay.ui.elements.VText

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EntitiyContextMenu() {
    if (!EntityContextMenuState.show) return

    var offset by remember { mutableStateOf(DpOffset.Zero) }

    var show by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onPointerEvent(PointerEventType.Move) {
                if (EntityContextMenuState.show && !show) {
                    val position = it.changes.first().position
                    offset = DpOffset(position.x.dp, position.y.dp)

                    show = true
                }
            },
        contentAlignment = Alignment.TopStart
    ) {
        Box(
            modifier = Modifier.offset(offset.x, offset.y).alpha(alphaMultiplier.value)
        ) {
            DropdownMenu(
                expanded = EntityContextMenuState.show,
                onDismissRequest = {
                    EntityContextMenuState.show = false
                    show = false
                },
                modifier = Modifier.background(color = Color(.1f, .1f, .1f, 0.9f))
            ) {
                ContextOptionsHeader()

                RefreshPlayersOption()

                RemovePlayerOption()
            }
        }
    }
}

@Composable
private fun ColumnScope.ContextOptionsHeader() {
    DropdownMenuItem(
        onClick = {},
        enabled = false
    ) {
        VText("Options for ${EntityContextMenuState.player?.name}", fontSize = TextUnit.Unspecified)
    }

    Divider(
        color = MainWhite.copy(.313f),
        modifier = Modifier.fillMaxWidth(.9f).align(Alignment.CenterHorizontally)
    )
}

@Composable
private fun RefreshPlayersOption() {
    var refreshText by remember { mutableStateOf("Refresh") }

    DropdownMenuItem(onClick = {
        refreshText = "Ah you called my bluff... this doesn't really work"
    }) {
        VText(refreshText, fontSize = TextUnit.Unspecified)
    }
}

@Composable
private fun RemovePlayerOption() {
    DropdownMenuItem(onClick = {
        Entities.remove(EntityContextMenuState.player?.name ?: "")
        EntityContextMenuState.show = false
    }) {
        VText("Remove", fontSize = TextUnit.Unspecified)
    }
}

object EntityContextMenuState {
    var show by mutableStateOf(false)
    var player by mutableStateOf<Entity?>(null)
}
