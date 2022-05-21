package com.voxyl.overlay.kindasortasomewhatviewmodelsishiguessithinkidkwhatevericantbebotheredsmh

import androidx.compose.runtime.mutableStateListOf
import com.voxyl.overlay.dataslashbusiness.events.*
import com.voxyl.overlay.ui.common.DisplayedEventState
import kotlinx.coroutines.*

object EventsToBeDisplayed {
    private val _events = mutableStateListOf<Event>()

    val events: List<Event>
        get() = _events

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

                if (_events.isEmpty()) {
                    delay(200)
                    continue
                }

                val event = _events[0]

                DisplayedEventState.setEvent(event)
                current = cs.launch {
                    delay(event.duration)
                    DisplayedEventState.cancel()
                }

                while (!event.cancelled) {
                    delay(200)
                }

                if (!paused) {
                    endCurrent()
                }

                delay(1000)
            }
        }

    }

    fun add(event: Event) {
        _events.add(event)
    }

    fun endCurrent() {
        current?.cancel()
        DisplayedEventState.cancel()
        _events -= _events[0]
    }

    private suspend fun pauseListener() {
        while (true) {
            delay(200)
            pause(ScreenShowing.screen != "main")
        }
    }

    private fun pause(pause: Boolean) {
        if (pause && !paused) {
            current?.cancel()
            DisplayedEventState.cancel()
            paused = true
        } else if (!pause && paused) {
            events.getOrNull(0)?.cancelled = false
            paused = false
        }
    }
}