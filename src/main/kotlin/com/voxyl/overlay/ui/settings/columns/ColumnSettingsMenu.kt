package com.voxyl.overlay.ui.settings.columns

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
import com.voxyl.overlay.ui.mainview.playerstats.StatsToShow
import com.voxyl.overlay.ui.mainview.playerstats.StatsToShow.clean
import com.voxyl.overlay.ui.theme.MainWhite
import com.voxyl.overlay.ui.theme.VText
import com.voxyl.overlay.ui.theme.alphaMultiplier

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ColumnSettingsMenu() {
    if (!ColumnSettingsMenuState.show) return

    var offset by remember { mutableStateOf(DpOffset.Zero) }
    var show by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onPointerEvent(PointerEventType.Move) {
                if (ColumnSettingsMenuState.show && !show) {
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
                expanded = ColumnSettingsMenuState.show,
                onDismissRequest = {
                    ColumnSettingsMenuState.show = false
                    show = false
                },
                modifier = Modifier.background(color = Color(.1f, .1f, .1f, 0.9f))
            ) {
                DropdownMenuItem(
                    onClick = {},
                    enabled = false
                ) {
                    VText("Options for ${ColumnSettingsMenuState.stat.clean()}", fontSize = TextUnit.Unspecified)
                }

                Divider(
                    color = MainWhite.copy(.313f),
                    modifier = Modifier.fillMaxWidth(.9f).align(Alignment.CenterHorizontally)
                )

                DropdownMenuItem(onClick = {
                    StatsToShow.remove(ColumnSettingsMenuState.stat)
                    ColumnSettingsMenuState.show = false
                }) {
                    VText("Remove", fontSize = TextUnit.Unspecified)
                }
            }
        }
    }
}

object ColumnSettingsMenuState {
    var show by mutableStateOf(false)
    var stat by mutableStateOf("")
}