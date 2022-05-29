package com.voxyl.overlay.business.discordrpc

import com.voxyl.overlay.business.playerfetching.player.Player
import com.voxyl.overlay.business.playerfetching.player.PlayerFactory
import com.voxyl.overlay.business.playerfetching.player.ResponseStatus
import com.voxyl.overlay.business.validation.popups.Error
import com.voxyl.overlay.business.validation.popups.Warning
import com.voxyl.overlay.kindasortasomewhatviewmodelsishiguessithinkidkwhatevericantbebotheredsmh.PopUpQueue
import com.voxyl.overlay.business.settings.config.Config
import de.jcm.discordgamesdk.Core
import de.jcm.discordgamesdk.CreateParams
import de.jcm.discordgamesdk.GameSDKException
import de.jcm.discordgamesdk.activity.Activity
import io.github.aakira.napier.Napier
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.io.IOException
import java.time.Instant

object DiscordRPC {
    private val startTime = Instant.now()
    private var player: Player? = null

    fun start(cs: CoroutineScope) = cs.launch (Dispatchers.IO){

        if (!initCore()) return@launch

        try {
            val params = CreateParams().apply {
                clientID = 979760216612675654L
                flags = CreateParams.Flags.toLong(CreateParams.Flags.NO_REQUIRE_DISCORD)
            }

            Core.initDownload()

            Core(params).use { core ->
                var i = 300000

                while (true) {
                    if (i % 3000 == 0) {
                        Activity().use { activity ->
                            if (player != null) {
                                activity.details = "${player?.get("name")}'s bwp stats:"
                                activity.state = "${player?.get("bwp.level")}✫ | ${player?.get("bwp.kills")} kills"
                            } else {
                                activity.details = "discord.gg/fBnfWXSDpu"
                            }

                            activity.timestamps().start = startTime

                            activity.assets().largeImage = "logo1024"
                            activity.assets().largeText = "Voxyl Overlay"

                            core.activityManager().updateActivity(activity)
                        }
                    }

                    if (i == 300000) {
                        refresh(cs)
                        i = 5
                    }

                    core.runCallbacks()

                    delay(5)

                    i += 5
                }
            }
        } catch (e: GameSDKException) {
            Napier.e("Error running callbacks", e)
            PopUpQueue.add(Warning("Overlay must be restarted after opening discord to show Discord Rich Presence"))
            return@launch
        }
    }

    private fun initCore(): Boolean {
        return try {
            Core.initDownload()
            true
        } catch (e: IOException) {
            PopUpQueue.add(Error("Error initializing Discord library; ${e.message}"))
            Napier.e("Error initializing Discord library; ${e.message}", e)
            false
        } catch (e: RuntimeException) {
            PopUpQueue.add(Error("Wtf OS are you using?? ${e.message}"))
            Napier.e("Wtf OS are you using?? ${e.message}", e)
            false
        } catch (e: UnsatisfiedLinkError) {
            PopUpQueue.add(Error("Error linking to Discord library; ${e.message}"))
            Napier.e("Error linking to Discord library; ${e.message}", e)
            false
        }
    }

    fun refresh(cs: CoroutineScope) {
        try {
            PlayerFactory.makePlayer(Config["player_name"] ?: "").onEach {
                if (it is ResponseStatus.Loaded) {
                    player = it.data as Player
                }
                if (it is ResponseStatus.Error) {
                    player = null
                }
            }.launchIn(cs)
        } catch (e: Exception) {
            player = null
        }
    }
}