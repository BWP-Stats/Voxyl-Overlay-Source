package com.voxyl.overlay.business.statsfetching.enitities

import com.voxyl.overlay.business.statsfetching.enitities.tags.*
import com.voxyl.overlay.business.settings.config.Aliases
import com.voxyl.overlay.business.settings.config.Config
import com.voxyl.overlay.business.settings.config.PlayerName
import com.voxyl.overlay.business.settings.config.ShowYourStatsInsteadOfAliases
import com.voxyl.overlay.controllers.playerstats.LeaderboardTracker

object TagGenerator {
    private val devNames = listOf("ambmt", "_lightninq", "vitroid", "firestarad", "sirjosh3917", "hero_of_gb", "rezcwa")

    fun generatePreTags(rawName: String): MutableList<Tag> {
        val name = if (
            Config[Aliases]
                .lowercase()
                .split(",")
                .contains(rawName.lowercase())
            && Config[ShowYourStatsInsteadOfAliases] == "true"
        ) Config[PlayerName] else rawName

        val tags = mutableListOf<Tag>()

        val isYouOrAlias = name.equals(Config[PlayerName], true) || Config[Aliases].lowercase().split(",").contains(rawName.lowercase())

        if (isYouOrAlias) tags += You
        if (name.lowercase() == "ambmt") tags += Ambmt
        if (name.lowercase() in devNames) tags += VoxylDev
        if (name.lowercase() == "carburettor") tags += OverlayDev

        return tags
    }

    fun generatePostTags(player: Entity): MutableList<Tag> {
        var tags = mutableListOf<Tag>()

        player["uuid"]?.let {
            val lvlLbPos = LeaderboardTracker.findInLevelLB(it)
            val wwLbPos = LeaderboardTracker.findInWWLB(it)

            if (lvlLbPos != null) tags += LevelLB(lvlLbPos["position"].asString ?: "")
            if (wwLbPos != null) tags += LevelLB(wwLbPos["position"].asString ?: "")

            if (tags.size == 2) tags = mutableListOf(
                LevelAndWWLB(
                    lvlLbPos?.get("position")?.asString ?: "",
                    wwLbPos?.get("position")?.asString ?: ""
                )
            )
        }

        return tags
    }
}
