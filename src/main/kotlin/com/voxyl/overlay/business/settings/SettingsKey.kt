package com.voxyl.overlay.business.settings

import java.util.*

abstract class SettingsKey<Type : Settings<Type>>(val name: String, default: Any) {
    val default = default.toString()

    fun isInvalidProperty(props: Properties) =
        props.getProperty(name).isNullOrBlank()
}
