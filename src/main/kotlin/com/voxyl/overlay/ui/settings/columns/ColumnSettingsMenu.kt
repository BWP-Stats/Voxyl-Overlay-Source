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
import com.voxyl.overlay.controllers.common.ui.MainWhite
import com.voxyl.overlay.controllers.common.ui.alphaMultiplier
import com.voxyl.overlay.controllers.playerstats.StatsToShow
import com.voxyl.overlay.ui.elements.VText
import com.voxyl.overlay.ui.entitystats.columns.Column.Companion.prettyName
import com.voxyl.overlay.ui.settings.columns.settings.NameSettings

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ColumnSettingsMenu() {
    if (!ColumnSettingsMenuState.shouldShow) return

    var offset by remember { mutableStateOf(DpOffset.Zero) }
    var show by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onPointerEvent(PointerEventType.Move) {
                if (ColumnSettingsMenuState.shouldShow && !show) {
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
                expanded = ColumnSettingsMenuState.shouldShow,
                onDismissRequest = {
                    ColumnSettingsMenuState.shouldShow = false
                    show = false
                },
                modifier = Modifier.background(color = Color(.1f, .1f, .1f, 0.9f))
            ) {
                DropdownMenuItem(
                    onClick = {},
                    enabled = false
                ) {
                    VText("Options for ${ColumnSettingsMenuState.dataString.prettyName()}", fontSize = TextUnit.Unspecified)
                }

                Divider(
                    color = MainWhite.copy(.313f),
                    modifier = Modifier.fillMaxWidth(.9f)
                        .align(Alignment.CenterHorizontally)
                )

                DropdownMenuItem(onClick = {
                    StatsToShow.remove(ColumnSettingsMenuState.dataString)
                    ColumnSettingsMenuState.shouldShow = false
                }) {
                    VText("Remove", fontSize = TextUnit.Unspecified)
                }

                NameSettings()
            }
        }
    }
}

object ColumnSettingsMenuState {
    var shouldShow by mutableStateOf(false)
    var dataString by mutableStateOf("")
}
