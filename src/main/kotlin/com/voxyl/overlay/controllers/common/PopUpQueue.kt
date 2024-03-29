package com.voxyl.overlay.controllers.common

import androidx.compose.runtime.*
import com.voxyl.overlay.business.validation.popups.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED

object PopUpQueue {
    object Current {
        var show by mutableStateOf(false)
        var popup by mutableStateOf<PopUp>(PopUp.empty())

        @JvmName("setEventSafe")
        fun setPopUp(popUp: PopUp?) {
            show = popUp != null
            if (popUp != null) {
                popup = popUp
            }
        }

        fun cancel() {
            setPopUp(null)
            popup.cancel()
        }
    }

    private var _popups = mutableStateListOf<PopUp>()

    val popups: List<PopUp?>
        get() = _popups

    private var job: Job? = null

    @OptIn(DelicateCoroutinesApi::class)
    fun start(cs: CoroutineScope = GlobalScope) = cs.launch {
        while (true) {
            while (_popups.isEmpty()) {
                delay(100)
                continue
            }

            val popup = _popups[0]
            Current.setPopUp(popup)

            job = launch {
                delay(popup.duration)
                Current.cancel()
            }

            try {
                job!!.join()
            } catch (_: CancellationException) {
                /* Expected */
            } finally {
                endCurrent()
            }
        }
    }

    fun add(popUp: PopUp) {
        _popups.add(popUp)
    }

    fun filter(tag: String) {
        if (popups.firstOrNull()?.tags?.contains(tag) == true) {
            endCurrent()
        }

        for (i in _popups.indices.reversed()) {
            if (tag in _popups[i].tags) {
                _popups.removeAt(i)
            }
        }
    }

    fun endCurrent() {
        job?.cancel()
        Current.cancel()
        if (_popups.isNotEmpty()) {
            _popups -= _popups[0]
        }
    }
}
