package com.voxyl.overlay.business.settings.window

import com.voxyl.overlay.business.settings.SettingsKey

class WindowStateKey(name: String, default: Any) : SettingsKey<WindowState>(name, default) {
    init { WindowState.register(this) }
}

val Width  = WindowStateKey(name = "saved_width",  default = "650")
val Height = WindowStateKey(name = "saved_height", default = "300")

val X = WindowStateKey(name = "saved_x", default = "0")
val Y = WindowStateKey(name = "saved_y", default = "0")

val IsAlwaysOnTop = WindowStateKey(name = "is_always_on_top", default = "true")
