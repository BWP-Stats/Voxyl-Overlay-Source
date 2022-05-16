package com.voxyl.overlay.ui.mainview.playerstats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.voxyl.overlay.data.player.PlayerState
import com.voxyl.overlay.middleman.PlayerKindaButNotExactlyViewModel
import com.voxyl.overlay.ui.theme.MainWhite
import com.voxyl.overlay.ui.theme.alphaMultiplier
import com.voxyl.overlay.ui.theme.am

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
                    Text("Options for ${PlayerContextMenuState.player?.name}", color = MainWhite)
                }

                Divider(color = MainWhite.copy(.2f))

                var refreshText by remember { mutableStateOf("Refresh") }

                DropdownMenuItem(onClick = {
                    refreshText = "Ah you called my bluff... this doesn't really work"
                }) {
                    Text(refreshText, color = MainWhite)
                }

                DropdownMenuItem(onClick = {
                    PlayerKindaButNotExactlyViewModel.remove(PlayerContextMenuState.player?.name ?: "")
                    PlayerContextMenuState.show = false
                }) {
                    Text("Remove", color = MainWhite)
                }
            }
        }
    }
}

object PlayerContextMenuState {
    var show by mutableStateOf(false)
    var player by mutableStateOf<PlayerState?>(null)
}