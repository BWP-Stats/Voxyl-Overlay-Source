package com.voxyl.overlay.ui.settings.columns

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
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
import com.voxyl.overlay.kindasortasomewhatviewmodelsishiguessithinkidkwhatevericantbebotheredsmh.StatsToShow
import com.voxyl.overlay.ui.theme.MainWhite
import com.voxyl.overlay.ui.elements.VText
import com.voxyl.overlay.ui.theme.alphaMultiplier

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ColumnsList() {
    if (!ColumnsListState.show) return

    var offset by remember { mutableStateOf(DpOffset.Zero) }
    var show by remember { mutableStateOf(false) }

    val addableStats = StatsToShow.addableStats

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onPointerEvent(PointerEventType.Move) {
                if (ColumnsListState.show && !show) {
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
                expanded = ColumnsListState.show,
                onDismissRequest = {
                    ColumnsListState.show = false
                    show = false
                },
                modifier = Modifier.background(color = Color(.1f, .1f, .1f, 0.9f))
            ) {
                DropdownMenuItem({}, enabled = false) {
                    VText("Addable columns", fontSize = TextUnit.Unspecified)
                }

                Divider(
                    color = MainWhite.copy(.313f),
                    modifier = Modifier.fillMaxWidth(.9f)
                        .align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.size(8.dp))

                DropdownMenuItem({}, enabled = false) {
                    LazyColumn(
                        modifier = Modifier.size(175.dp, 40.dp + (addableStats.size * 20).dp)
                    ) {
                        for (stat in addableStats) {
                            stat(stat)
                        }
                    }
                }
            }
        }
    }
}

val statsWithAdditionalSettings = listOf(
    "name"
)

private fun LazyListScope.stat(item: StatsToShow.Stat) {
    item {
        Box(
            modifier = Modifier.fillMaxSize()
                .clickable {
                    StatsToShow.add(item.raw)
                }
        ) {
            Spacer(modifier = Modifier.size(2.dp))
            VText(
                item.clean + (if (item.raw in statsWithAdditionalSettings) "*" else ""),
                fontSize = TextUnit.Unspecified
            )
        }
    }
}

object ColumnsListState {
    var show by mutableStateOf(false)
}