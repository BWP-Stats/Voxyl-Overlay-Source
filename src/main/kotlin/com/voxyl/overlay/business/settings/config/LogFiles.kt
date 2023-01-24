package com.voxyl.overlay.business.settings.config

enum class LogFiles(val path: String) {
    Badlion(
        System.getenv("APPDATA") + "\\.minecraft\\logs\\blclient\\minecraft\\latest.log"
    ),
    Lunar(
        System.getProperty("user.home") + "\\.lunarclient\\offline\\multiver\\logs\\latest.log"
    ),
    Vanilla(
        System.getenv("APPDATA") + "\\.minecraft\\logs\\latest.log"
    );
}
