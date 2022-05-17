package com.voxyl.overlay.settings.config

enum class ConfigKeys(var key: String, var defaultValue: String) {
    HypixelApiKey("hypixel_api_key", ""),
    BwpApiKey("bwp_api_key", ""),
    LogFilePath("log_file_path", ""),
    PlayerName("player_name", ""),

    AddYourselfToOverlay("add_yourself_to_overlay", "true"),
    PinYourselfToTop("pin_yourself_to_top", "false"),

    AutoShowAndHide("auto_show_and_hide", "false"),
    AutoShowAndHideDelay("auto_show_and_hide_delay", "5"),

    Opacity("opacity", "1"),
    TitleBarSizeMulti("title_bar_size_multi", "1"),

    CenterStats("center_stats", "false"),

    ShowRankPrefix("show_rank_prefix", "true"),

    R("r", "130"),
    G("g", "32"),
    B("b", "229"),

    OpenAndCloseKeybind("open_and_close_keybind", "Set a key"),
    ClearPlayersKeybind("clear_players_keybind", "Set a key"),
}
