package com.voxyl.overlay.ui.common

import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.voxyl.overlay.kindasortasomewhatviewmodelsishiguessithinkidkwhatevericantbebotheredsmh.PopUpQueue
import com.voxyl.overlay.ui.theme.MainWhite
import com.voxyl.overlay.ui.theme.VText

@Composable
fun BoxScope.PopUpBar() {
    val offset by animateFloatAsState(
        if (PopUpQueue.Current.show) -10f else 100f
    )

    val event = PopUpQueue.Current.popUp

    Row(
        modifier = Modifier.size(400.dp, 50.dp)
            .align(Alignment.BottomCenter)
            .absoluteOffset(y = offset.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(event.color)
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        event.icon(Modifier.size(30.dp))

        Spacer(modifier = Modifier.size(10.dp))

        VText(
            text = event.text,
            fontSize = TextUnit.Unspecified,
            modifier = Modifier.weight(1f)
        )

        Button(
            onClick = if (event.isConfirmation) {
                { event.onConfirmationClick() }
            } else {
                { PopUpQueue.Current.cancel() }
            },
            modifier = Modifier.size(30.dp),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Transparent
            ),
            elevation = null
        ) {
            Icon(
                imageVector = if (event.isConfirmation) Icons.Filled.Check else Icons.Filled.Close,
                contentDescription = "Close Pop-up",
                tint = MainWhite,
                modifier = Modifier.requiredSize(30.dp)
            )
        }
    }
}