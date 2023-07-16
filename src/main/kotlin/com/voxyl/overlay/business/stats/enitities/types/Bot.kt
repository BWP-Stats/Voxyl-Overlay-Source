package com.voxyl.overlay.business.stats.enitities.types

class Bot(name: String) : RawEntity(name) {
    init {
        data["name"] = name

        data["bwp.role"] = "BOT"
        data["hypixel.rank"] = data["bwp.role"]!!

        data["hypixel.rankColor"] = "dark-gray"
    }
}
