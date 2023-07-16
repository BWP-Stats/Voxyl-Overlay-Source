@file:Suppress("MemberVisibilityCanBePrivate")

package com.voxyl.overlay.business.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import java.util.*

abstract class StateRotator<T>(val originalStates: Array<out T>) {
    abstract val states: List<T>

    fun get(): T {
        return states.first()
    }

    fun rotate() {
        Collections.rotate(states, -1)
    }

    val first: T
        get() = originalStates.first()

    val last: T
        get() = originalStates.last()
}

class NormalStateRotator<T>(vararg states: T): StateRotator<T>(states) {
    override val states = states.toList()
}

class ComposeStateRotator<T>(vararg states: T): StateRotator<T>(states) {
    private val _states = states.toList().toMutableStateList()

    override val states
        get() = _states
}

@Composable
fun <T> rememberComposeStateRotatorOf(vararg states: T) =
    remember { ComposeStateRotator(*states) }
