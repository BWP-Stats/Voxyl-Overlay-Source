@file:Suppress("FunctionName", "ObjectPropertyName")
@file:OptIn(ExperimentalComposeUiApi::class)

package com.voxyl.overlay

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import com.voxyl.overlay.data.apis.ApiProvider
import com.voxyl.overlay.middleman.PlayerKindaButNotExactlyViewModel
import com.voxyl.overlay.ui.common.MainScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.awt.Dimension

@ExperimentalComposeUiApi
@Preview
fun main() = application {

    Window(
        onCloseRequest = ::exitApplication,
        undecorated = true,
        transparent = true,
        title = "VoxylOverlay",
        icon = painterResource("VoxylLogoIcon.ico"),
        state = WindowState(
            size = getPreferredWindowSize(650, 300),
            position = getPreferredWindowPosition(WindowPosition.PlatformDefault)
        )
    ) {
        window.minimumSize = Dimension(400, 200)

        /*val cs = rememberCoroutineScope { Dispatchers.IO }

        cs.launch {
            delay(3000)
            PlayerKindaButNotExactlyViewModel.add("_lightninq", cs)
            PlayerKindaButNotExactlyViewModel.add("carburettor", cs)
            PlayerKindaButNotExactlyViewModel.add("ambmt", cs)
            PlayerKindaButNotExactlyViewModel.add("your mom", cs)
        }*/

        MainScreen(this)
    }
}

//TODO: Return window position from previous usage
fun getPreferredWindowPosition(default: WindowPosition.PlatformDefault): WindowPosition {
    return default
}

//TODO: Return window size from previous usage
fun getPreferredWindowSize(desiredWidth: Int, desiredHeight: Int): DpSize {
    return DpSize(desiredWidth.dp, desiredHeight.dp)
}
