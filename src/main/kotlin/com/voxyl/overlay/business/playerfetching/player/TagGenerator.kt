package com.voxyl.overlay.business.playerfetching.player

import com.voxyl.overlay.business.playerfetching.player.tags.*
import com.voxyl.overlay.kindasortasomewhatviewmodelsishiguessithinkidkwhatevericantbebotheredsmh.LeaderboardTrackerWhatEvenIsAViewModel
import com.voxyl.overlay.business.settings.config.Config
import com.voxyl.overlay.business.settings.config.ConfigKeys

object TagGenerator {
    private val devNames = listOf("ambmt", "_lightninq", "vitroid", "firestarad", "sirjosh3917", "hero_of_gb", "rezcwa")

    fun generatePreTags(name: String, rawName: String): MutableList<Tag> {
        val tags = mutableListOf<Tag>()

        val isYouOrAlias = name.equals(Config[ConfigKeys.PlayerName], true) || Config["aliases"]?.lowercase()?.split(",")
            ?.contains(rawName.lowercase()) == true

        if (isYouOrAlias) tags += You
        if (name.lowercase() == "ambmt") tags += Ambmt
        if (name.lowercase() in devNames) tags += VoxylDev
        if (name.lowercase() == "carburettor") tags += OverlayDev

        return tags
    }

    fun generatePostTags(player: PlayerState): MutableList<Tag> {
        var tags = mutableListOf<Tag>()

        val lvlLbPos = LeaderboardTrackerWhatEvenIsAViewModel.findInLevelLB(player["uuid"]!!)
        val wwLbPos = LeaderboardTrackerWhatEvenIsAViewModel.findInWWLB(player["uuid"]!!)

        if (lvlLbPos != null) tags += LevelLB(lvlLbPos["position"].asString ?: "")
        if (wwLbPos != null) tags += LevelLB(wwLbPos["position"].asString ?: "")

        if (tags.size == 2) tags = mutableListOf(
            LevelAndWWLB(
                lvlLbPos?.get("position")?.asString ?: "",
                wwLbPos?.get("position")?.asString ?: ""
            )
        )
        return tags
    }
}