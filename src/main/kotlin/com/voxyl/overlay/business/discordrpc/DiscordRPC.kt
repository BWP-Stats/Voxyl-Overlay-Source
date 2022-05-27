package com.voxyl.overlay.business.discordrpc

import com.voxyl.overlay.business.networking.player.Player
import com.voxyl.overlay.business.networking.player.PlayerFactory
import com.voxyl.overlay.business.networking.player.Status
import com.voxyl.overlay.business.validation.popups.Error
import com.voxyl.overlay.business.validation.popups.Warning
import com.voxyl.overlay.kindasortasomewhatviewmodelsishiguessithinkidkwhatevericantbebotheredsmh.PopUpQueue
import com.voxyl.overlay.settings.config.Config
import de.jcm.discordgamesdk.Core
import de.jcm.discordgamesdk.CreateParams
import de.jcm.discordgamesdk.GameSDKException
import de.jcm.discordgamesdk.activity.Activity
import io.github.aakira.napier.Napier
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.io.File
import java.io.IOException
import java.time.Instant

object DiscordRPC {
    private var player: Player? = null
    private var wait = true

    suspend fun `try`(cs: CoroutineScope) {

        if (!initCore()) return

        try {
            CreateParams().use { params ->
                params.clientID = 979760216612675654L
                params.flags = CreateParams.Flags.toLong(CreateParams.Flags.NO_REQUIRE_DISCORD)
                Core(params).use { core ->
                    var i = 300000

                    while (true) {
                        if (i == 300000) {
                            getPlayerStats(cs)

                            while (wait) {
                                delay(100)
                            }

                            Activity().use { activity ->
                                if (player != null) {
                                    activity.details = "${player?.get("name")}'s bwp stats:"
                                    activity.state = "${player?.get("bwp.level")}✫ | ${player?.get("bwp.kills")} kills"
                                } else {
                                    activity.details = ".gg/fBnfWXSDpu"
                                    activity.state = ""
                                }

                                activity.timestamps().start = Instant.now()

                                activity.assets().largeImage = "logo1024"

                                core.activityManager().updateActivity(activity)
                            }

                            i = 0
                        }

                        core.runCallbacks()

                        delay(2000)

                        i += 2000
                    }
                }
            }
        } catch (e: GameSDKException) {
            Napier.e("Error running callbacks", e)
            PopUpQueue.add(Warning("Overlay must be restarted after opening discord to show Discord Rich Presence"))
            return
        }
    }

    private fun initCore(): Boolean {
        return try {
            val discordLibrary = NativeLibraryDownloader.downloadDiscordLibrary()
                ?: throw IOException("Could not download discord library")

            if (!discordLibrary.exists()) throw IOException("...File doesn't exit?")

            Core.init(discordLibrary)
            true
        } catch (e: IOException) {
            PopUpQueue.add(Error("Error initializing Discord library; ${e.message}"))
            Napier.e("Error initializing Discord library; ${e.message}", e)
            false
        } catch (e: RuntimeException) {
            PopUpQueue.add(Error("Wtf OS are you using; ${e.message}"))
            Napier.e("Wtf OS are you using; ${e.message}", e)
            false
        }
    }

    private fun getPlayerStats(cs: CoroutineScope) {
        wait = true

        try {
            PlayerFactory.makePlayer(Config["player_name"] ?: "").onEach {
                if (it is Status.Loaded) {
                    player = it.data as Player
                    wait = false
                }
                if (it is Status.Error) {
                    player = null
                    wait = false
                }
            }.launchIn(cs)
        } catch (e: Exception) {
            player = null
            wait = false
        }
    }
}