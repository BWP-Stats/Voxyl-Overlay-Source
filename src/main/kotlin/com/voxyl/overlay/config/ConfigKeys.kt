package com.voxyl.overlay.config

enum class ConfigKeys(var key: String, var defaultValue: String) {
    HYPIXEL_API_KEY("hypixel_api_key", ""),
    BWP_API_KEY("bwp_api_key", ""),
    LOG_FILE_PATH("log_file_path", ""),
    PLAYER_NAME("player_name", ""),

    ADD_YOURSELF_TO_OVERLAY("add_yourself_to_overlay", "true"),
    PIN_YOURSELF_TO_TOP("pin_yourself_to_top", "false"),
    AUTO_SHOW_AND_HIDE("auto_show_and_hide", "false"),
    AUTO_SHOW_AND_HIDE_DELAY("auto_show_and_hide_delay", "5"),
}