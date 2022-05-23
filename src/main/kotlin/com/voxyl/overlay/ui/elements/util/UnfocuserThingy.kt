package com.voxyl.overlay.ui.elements.util

import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.pointer.consumeDownChange
import androidx.compose.ui.input.pointer.pointerInput


//This exists to take the focus off of the text box as Idk how to do it otherwise
internal fun Modifier.requestFocusOnClick(itShouldWork: Boolean = true): Modifier = composed {

    if (!itShouldWork) {
        return@composed this
    }

    val focusRequester = remember(::FocusRequester)

    focusRequester(focusRequester).focusable().pointerInput(Unit) {
        forEachGesture {
            awaitPointerEventScope {
                val down = awaitFirstDown()
                focusRequester.requestFocus()
                down.consumeDownChange()
            }
        }
    }
}