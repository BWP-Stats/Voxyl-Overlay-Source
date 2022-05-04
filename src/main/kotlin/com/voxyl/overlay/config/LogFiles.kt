package com.voxyl.overlay.config

enum class LogFiles(val path: String) {

    //check . vs no . on mac
    //C:\Users\wanna\AppData\Roaming\.minecraft\logs\blclient\minecraft\latest.log
    Badlion(System.getenv("APPDATA") + "\\.minecraft\\logs\\blclient\\minecraft\\latest.log"),  //check version

    //C:\Users\wanna\.lunarclient\offline\1.8\logs\latest.log
    Lunar(System.getProperty("user.home") + "\\.lunarclient\\offline\\1.8\\logs\\latest.log"),

    //C:\Users\wanna\AppData\Roaming\.minecraft\logs\latest.log
    Vanilla(
        System.getenv("APPDATA") +
                (if ("MacOS".equals(
                        System.getProperty("os.name"),
                        ignoreCase = true
                    )
                ) "\\minecraft" else "\\.minecraft") + "\\logs\\latest.log"
    );

}