package com.voxyl.overlay.config

enum class ConfigKeys(var key: String, var defaultValue: String) {
    HypixelApiKey("hypixel_api_key", ""),
    BwpApiKey("bwp_api_key", ""),
    LogFilePath("log_file_path", ""),
    PlayerName("player_name", ""),

    AddYourselfToOverlay("add_yourself_to_overlay", "true"),
    PinYourselfToTop("pin_yourself_to_top", "false"),

    AutoShowAndHide("auto_show_and_hide", "false"),
    AutoShowAndHideDelay("auto_show_and_hide_delay", "5"),
}