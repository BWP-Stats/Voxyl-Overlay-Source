package com.voxyl.overlay.ui.common

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.voxyl.overlay.kindasortasomewhatviewmodelsishiguessithinkidkwhatevericantbebotheredsmh.PopUpQueue
import com.voxyl.overlay.ui.theme.MainWhite
import com.voxyl.overlay.ui.theme.VText
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@OptIn(DelicateCoroutinesApi::class)
@Composable
fun BoxScope.PopUpBar() {
    val offset by animateFloatAsState(
        if (PopUpQueue.Current.show) -10f else 100f
    )

    val popUp = PopUpQueue.Current.popUp

    Row(
        modifier = Modifier.size(400.dp, 62.dp)
            .align(Alignment.BottomCenter)
            .absoluteOffset(y = offset.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(popUp.color)
            .absolutePadding(right = 10.dp, left = 10.dp, bottom = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        popUp.icon(Modifier.size(30.dp))

        Spacer(modifier = Modifier.size(10.dp))

        VText(
            text = popUp.text,
            fontSize = TextUnit.Unspecified,
            modifier = Modifier.weight(1f)
        )

        Button(
            onClick = popUp.onClick,
            modifier = Modifier.size(30.dp),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Transparent
            ),
            elevation = null
        ) {
            Icon(
                imageVector = popUp.clickableIcon,
                contentDescription = "Close Pop-up",
                tint = MainWhite,
                modifier = Modifier.requiredSize(30.dp)
            )
        }
    }

    val elapsedTime = remember { Animatable(1f) }

    val cs = rememberCoroutineScope()

    DisposableEffect(PopUpQueue.Current.show) {
        cs.launch {
            elapsedTime.animateTo(0f, animationSpec = tween(
                durationMillis = popUp.duration.toInt(),
                easing = LinearEasing
            ))
        }

        onDispose {
            GlobalScope.launch {
                elapsedTime.snapTo(1f)
            }
        }
    }

    Box(
        modifier = Modifier.size(376.dp, 8.dp)
            .absoluteOffset(y = offset.dp - 6.dp)
            .align(Alignment.BottomCenter)
            .clip(RoundedCornerShape(8.dp))
            .background(
                popUp.color.copy(
                    popUp.color.alpha / 2f,
                    (popUp.color.red + 0.1f).coerceAtMost(1f),
                    (popUp.color.green + 0.1f).coerceAtMost(1f),
                    (popUp.color.blue + 0.1f).coerceAtMost(1f),
                )
            )
    ) {
        Box(
            modifier = Modifier.fillMaxHeight()
                .fillMaxWidth(elapsedTime.value)
                .clip(RoundedCornerShape(8.dp))
                .background(
                    popUp.color.copy(
                        popUp.color.alpha,
                        (popUp.color.red + 0.1f).coerceAtMost(1f),
                        (popUp.color.green + 0.1f).coerceAtMost(1f),
                        (popUp.color.blue + 0.1f).coerceAtMost(1f),
                    )
                )
        )
    }
}