package com.voxyl.overlay.ui.settings.columns

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.mouseClickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.isSecondaryPressed
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.voxyl.overlay.AppWindow
import com.voxyl.overlay.business.settings.config.*
import com.voxyl.overlay.controllers.common.ui.MainWhite
import com.voxyl.overlay.controllers.common.ui.am
import com.voxyl.overlay.controllers.playerstats.StatsToShow
import com.voxyl.overlay.ui.elements.VText
import com.voxyl.overlay.ui.entitystats.stats.Statistic
import com.voxyl.overlay.ui.entitystats.stats.Statistic.Companion.prettyName
import org.burnoutcrew.reorderable.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ColumnsSettings() {
    val state = rememberReorderState()
    val stats = StatsToShow._stats

    val width = (AppWindow.width.dp - 40.dp) / stats.size

    Column(
        Modifier
            .height(82.dp)
            .fillMaxWidth()
            .absolutePadding(right = 20.dp, left = 20.dp),
    ) {
        LazyRow(
            state = state.listState,
            modifier = Modifier
                .height(32.dp)
                .fillMaxWidth()
                .background(Color(.2f, .2f, .2f, .3f).am)
                .reorderable(
                    state,
                    { from, to -> stats.move(from.index, to.index) },
                    orientation = Orientation.Horizontal,
                    onDragEnd = { _, _ ->
                        Config[Columns] = stats.joinToString(",")
                    }
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(items = stats, { it }) {
                Box(
                    Modifier
                        .width(width)
                        .fillMaxHeight()
                        .draggedItem(
                            state.offsetByKey(it),
                            Orientation.Horizontal
                        )
                        .detectReorder(state)
                        .mouseClickable {
                            if (buttons.isSecondaryPressed) {
                                ColumnSettingsMenuState.dataString = it
                                ColumnSettingsMenuState.shouldShow = true
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    VText(
                        text = it.prettyName() + (if (Statistic.getMetadataForDataString(it).hasAdditionalSettings) "*" else ""),
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp
                    )
                }
            }
        }

        Row(
            Modifier
                .height(50.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(Modifier.weight(1f))
            Column(
                Modifier.fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                VText(
                    "You can drag the columns to reorder them, or right click on one to remove/modify it (ᴮ = BWP, ᴴ = Hypixel)",
                    fontSize = 15.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                VText(
                    "Settings with * have additional settings which you can right click on to access",
                    fontSize = 11.sp
                )
            }
            Spacer(Modifier.weight(2f))
            Button(
                onClick = {
                    ColumnsListState.show = true
                },
                modifier = Modifier
                    .requiredSize(42.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(.2f, .2f, .2f, .3f).am
                ),
                elevation = null,
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    tint = MainWhite,
                    contentDescription = "Add column",
                    modifier = Modifier.requiredSize(36.dp)
                )
                ColumnsList()
            }
            Spacer(modifier = Modifier.size(20.dp))
        }
    }

    Column(
        Modifier
            .height(32.dp)
            .fillMaxWidth()
            .absolutePadding(right = 20.dp, left = 20.dp)
            .offset(y = -82.dp)
    ) {
        ColumnSettingsMenu()
    }
}
