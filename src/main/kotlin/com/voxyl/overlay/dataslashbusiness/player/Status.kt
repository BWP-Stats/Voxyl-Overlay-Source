package com.voxyl.overlay.dataslashbusiness.player

sealed class Status<T>(val name: String, val data: T? = null, val message: String? = null) {
    class Loading<T>(data: T? = null, name: String) : Status<T>(name, data)
    class Loaded<T>(data: T, name: String) : Status<T>(name, data)
    class Error<T>(message: String, data: T? = null, name: String) : Status<T>(name, data, message)
}