package com.voxyl.overlay.business.statsfetching.enitities

sealed class ResponseStatus<T>(val name: String, val data: T? = null, val message: String? = null) {
    class Loading<T>(
        data: T? = null,
        name: String
    ) : ResponseStatus<T>(name, data)

    class Loaded<T>(
        data: T,
        name: String
    ) : ResponseStatus<T>(name, data)

    class Error<T>(
        message: String,
        data: T? = null,
        name: String
    ) : ResponseStatus<T>(name, data, message)
}
