package com.voxyl.overlay.business.networking.valueclasses

import com.google.gson.JsonObject

@JvmInline
value class GameStatsJson(val json: JsonObject) {
    fun toOverallGameStats(): Map<String, String> {
        var beds = 0
        var wins = 0
        var kills = 0
        var finals = 0

        val jsonKeySet = json.getAsJsonObject("stats").keySet()

        for (i in 0 until jsonKeySet.size) {
            val jsonNode = json.getAsJsonObject("stats").getAsJsonObject(jsonKeySet.elementAt(i))

            beds += jsonNode.get("beds")?.asInt ?: 0
            wins += jsonNode.get("wins")?.asInt ?: 0
            kills += jsonNode.get("kills")?.asInt ?: 0
            finals += jsonNode.get("finals")?.asInt ?: 0
        }

        return mapOf("beds" to "$beds", "wins" to "$wins", "kills" to "$kills", "finals" to "$finals")
    }

    override fun toString() = json.toString()
}