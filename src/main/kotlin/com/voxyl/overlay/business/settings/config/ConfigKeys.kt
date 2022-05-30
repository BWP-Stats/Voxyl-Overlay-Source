package com.voxyl.overlay.business.settings.config

enum class ConfigKeys(val key: String, var defaultValue: String) {
    HypixelApiKey("hypixel_api_key", ""),
    BwpApiKey("bwp_api_key", ""),
    LogFilePath("log_file_path", ""),
    PlayerName("player_name", ""),

    AddYourselfToOverlay("add_yourself_to_overlay", "true"),
    PinYourselfToTop("pin_yourself_to_top", "true"),

    AutoShowAndHide("auto_show_and_hide", "false"),
    AutoShowAndHideDelay("auto_show_and_hide_delay", "5"),

    Opacity("opacity", "1"),
    BackgroundOpacity("background_opacity", "1"),
    TitleBarSizeMulti("title_bar_size_multi", "1"),

    CenterStats("center_stats", "false"),
    RankPrefix("rank_prefix", "bwp"),
    ShowRankPrefix("rank_prefix", "true"),

    R("r", "130"),
    G("g", "32"),
    B("b", "229"),

    OpenAndCloseKeybind("open_and_close_keybind", "Set a key"),
    ClearPlayersKeybind("clear_players_keybind", "Set a key"),
    RefreshPlayersKeybind("refresh_players_keybind", "Set a key"),

    SortBy("sort_by", "name"),
    SortAsc("sort_asc", "false"),

    Columns("columns", "tags,bwp.level,name,bwp.wins,bwp.kills,bwp.finals"),

    Aliases("aliases", ""),
    ShowYourStatsInsteadOfAliases("show_your_stats_instead_of_aliases", "false"),

    ShowDiscordRP("show_discord_rp", "true"),

    UseBackupBwpApi("use_backup_bwp_api", "true"),
}