package com.voxyl.overlay.business.settings.misc

import com.voxyl.overlay.business.settings.SettingsKey

class MiscKey(name: String, default: Any) : SettingsKey<MiscSettings>(name, default) {
    init { MiscSettings.register(this) }
}

val FirstTimeUsingOverlay  = MiscKey(name = "first_time",                default =  "true")
val FirstTimeSwitchingRank = MiscKey(name = "first_time_switching_rank", default =  "true")

val CurrentVersion = MiscKey(name = "current_version", default = "")
