package com.voxyl.overlay.settings.window

enum class SavedWindowStateKeys(var key: String, var defaultValue: String) {
    Width("saved_width", "650"),
    Height("saved_height", "300"),

    X("saved_x", "0"),
    Y("saved_y", "0"),

    IsAlwaysOnTop("is_always_on_top", "true"),
}