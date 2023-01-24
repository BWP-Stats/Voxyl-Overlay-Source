package com.voxyl.overlay.ui.playerstats.playerstats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
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
import com.voxyl.overlay.business.playerfetching.player.PlayerState
import com.voxyl.overlay.controllers.common.ui.MainWhite
import com.voxyl.overlay.controllers.common.ui.alphaMultiplier
import com.voxyl.overlay.controllers.playerstats.Players
import com.voxyl.overlay.ui.elements.VText

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PlayerContextMenu() {
    if (!PlayerContextMenuState.show) return

    var offset by remember { mutableStateOf(DpOffset.Zero) }

    var show by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onPointerEvent(PointerEventType.Move) {
                if (PlayerContextMenuState.show && !show) {
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
                expanded = PlayerContextMenuState.show,
                onDismissRequest = {
                    PlayerContextMenuState.show = false
                    show = false
                },
                modifier = Modifier.background(color = Color(.1f, .1f, .1f, 0.9f))
            ) {
                DropdownMenuItem(
                    onClick = {},
                    enabled = false
                ) {
                    VText("Options for ${PlayerContextMenuState.player?.name}", fontSize = TextUnit.Unspecified)
                }

                Divider(
                    color = MainWhite.copy(.313f),
                    modifier = Modifier.fillMaxWidth(.9f).align(Alignment.CenterHorizontally)
                )

                var refreshText by remember { mutableStateOf("Refresh") }

                DropdownMenuItem(onClick = {
                    refreshText = "Ah you called my bluff... this doesn't really work"
                }) {
                    VText(refreshText, fontSize = TextUnit.Unspecified)
                }

                DropdownMenuItem(onClick = {
                    Players.remove(PlayerContextMenuState.player?.name ?: "")
                    PlayerContextMenuState.show = false
                }) {
                    VText("Remove", fontSize = TextUnit.Unspecified)
                }
            }
        }
    }
}

object PlayerContextMenuState {
    var show by mutableStateOf(false)
    var player by mutableStateOf<PlayerState?>(null)
}
