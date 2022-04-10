package com.voxyl.overlay.config

enum class LogFiles(val filePath: String) {

    //check . vs no . on mac
    //C:\Users\wanna\AppData\Roaming\.minecraft\logs\blclient\minecraft\latest.log
    BADLION(System.getenv("APPDATA") + "\\.minecraft\\logs\\blclient\\minecraft\\latest.log"),  //check version

    //C:\Users\wanna\.lunarclient\offline\1.8\logs\latest.log
    LUNAR(System.getProperty("user.home") + "\\.lunarclient\\offline\\1.8\\logs\\latest.log"),

    //C:\Users\wanna\AppData\Roaming\.pvplounge\logs\latest.log
    PVP_LOUNGE(System.getenv("APPDATA") + "\\.pvplounge\\logs\\latest.log"),

    //C:\Users\wanna\AppData\Roaming\.minecraft\logs\latest.log
    VANILLA(
        System.getenv("APPDATA") +
                (if ("MacOS".equals(
                        System.getProperty("os.name"),
                        ignoreCase = true
                    )
                ) "\\minecraft" else "\\.minecraft") + "\\logs\\latest.log"
    );

}