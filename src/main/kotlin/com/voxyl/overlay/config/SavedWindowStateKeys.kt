package com.voxyl.overlay.config

enum class SavedWindowStateKeys(var key: String, var defaultValue: String) {
    SAVED_WIDTH("saved_width", "650"),
    SAVED_HEIGHT("saved_height", "300"),

    SAVED_X("saved_x", "0"),
    SAVED_Y("saved_y", "0"),

    IS_ALWAYS_ON_TOP("is_always_on_top", "false"),
}