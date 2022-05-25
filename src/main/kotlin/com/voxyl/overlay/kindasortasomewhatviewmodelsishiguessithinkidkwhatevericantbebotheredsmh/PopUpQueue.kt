package com.voxyl.overlay.kindasortasomewhatviewmodelsishiguessithinkidkwhatevericantbebotheredsmh

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.voxyl.overlay.business.validation.popups.*
import kotlinx.coroutines.*

object PopUpQueue {
    object Current {
        var show by mutableStateOf(false)
        var popUp by mutableStateOf<PopUp>(PopUp.empty())

        @JvmName("setEventSafe")
        fun setPopUp(popUp: PopUp?) {
            show = popUp != null
            if (popUp != null) {
                this.popUp = popUp
            }
        }

        fun cancel() {
            setPopUp(null)
            popUp.cancel()
        }
    }


    private val _popups = mutableStateListOf<PopUp>()

    val popups: List<PopUp>
        get() = _popups

    private var current: Job? = null

    private var paused = false

    @OptIn(DelicateCoroutinesApi::class)
    fun start(cs: CoroutineScope = GlobalScope) {
        cs.launch(Dispatchers.Default) { pauseListener() }

        cs.launch(Dispatchers.Default) {
            while (true) {
                while (paused) {
                    delay(100)
                }

                if (_popups.isEmpty()) {
                    delay(200)
                    continue
                }

                delay(750)

                val event = _popups[0]

                Current.setPopUp(event)
                current = cs.launch {
                    delay(event.duration)
                    Current.cancel()
                }

                while (!event.cancelled) {
                    delay(200)
                }

                if (!paused) {
                    endCurrent()
                }
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
        _popups.filter { tag in it.tags }
    }

    private fun endCurrent() {
        current?.cancel()
        Current.cancel()
        _popups -= _popups[0]
    }

    private suspend fun pauseListener() {
        while (true) {
            delay(200)
            pause(ScreenShowing.screen != "playerstats")
        }
    }

    private fun pause(pause: Boolean) {
        if (pause && !paused) {
            current?.cancel()
            Current.cancel()
            paused = true
        } else if (!pause && paused) {
            popups.getOrNull(0)?.cancelled = false
            paused = false
        }
    }
}