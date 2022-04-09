package com.voxyl.overlay.data.player

sealed class Status<T>(val playerName: String, val data: T? = null, val message: String? = null) {
    class Loading<T>(data: T? = null, playerName: String) : Status<T>(playerName, data)
    class Loaded<T>(data: T, playerName: String) : Status<T>(playerName, data)
    class Error<T>(message: String, data: T? = null, playerName: String) : Status<T>(playerName, data, message)
}