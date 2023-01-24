package com.voxyl.overlay.business.settings.config

import com.voxyl.overlay.business.settings.SettingsKey

class ConfigKey(name: String, default: Any) : SettingsKey<Config>(name, default) {
    init { Config.register(this) }
}

val HypixelApiKey = ConfigKey(name = "hypixel_api_key", default = "")
val BwpApiKey     = ConfigKey(name = "bwp_api_key",     default = "")
val LogFilePath   = ConfigKey(name = "log_file_path",   default = "")
val PlayerName    = ConfigKey(name = "player_name",     default = "")

val AddYourselfToOverlay = ConfigKey(name = "add_yourself_to_overlay", default = "true")
val PinYourselfToTop     = ConfigKey(name = "pin_yourself_to_top",     default = "true")

val AutoShowAndHide      = ConfigKey(name = "auto_show_and_hide",       default = "false")
val AutoShowAndHideDelay = ConfigKey(name = "auto_show_and_hide_delay", default = "5")

val Opacity           = ConfigKey(name = "opacity",              default = "1.2")
val BackgroundOpacity = ConfigKey(name = "background_opacity",   default = "1.5")
val TitleBarSizeMulti = ConfigKey(name = "title_bar_size_multi", default = "0.9")

val CenterStats    = ConfigKey(name = "center_stats", default = "false")
val RankPrefix     = ConfigKey(name = "rank_prefix",  default = "bwp")
val ShowRankPrefix = ConfigKey(name = "rank_prefix",  default = "true")

val PrimaryColorR = ConfigKey(name = "r", default = "130")
val PrimaryColorG = ConfigKey(name = "g", default = "32")
val PrimaryColorB = ConfigKey(name = "b", default = "229")

val OpenAndCloseKeybind   = ConfigKey(name = "open_and_close_keybind",  default = "Set a key")
val ClearPlayersKeybind   = ConfigKey(name = "clear_players_keybind",   default = "Set a key")
val RefreshPlayersKeybind = ConfigKey(name = "refresh_players_keybind", default = "Set a key")

val SortBy  = ConfigKey(name = "sort_by",  default = "name")
val SortAsc = ConfigKey(name = "sort_asc", default = "false")

val Columns = ConfigKey(name = "columns", default = "tags,bwp.level,name,bwp.wins,bwp.kills,bwp.finals")

val Aliases                       = ConfigKey(name = "aliases",                            default = "")
val ShowYourStatsInsteadOfAliases = ConfigKey(name = "show_your_stats_instead_of_aliases", default = "false")

val ShowDiscordRP = ConfigKey(name = "show_discord_rp", default = "true")

val AddBotsToOverlay = ConfigKey(name = "add_bots_to_overlay", default = "true")
