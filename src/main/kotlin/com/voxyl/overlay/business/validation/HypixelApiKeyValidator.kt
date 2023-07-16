package com.voxyl.overlay.business.validation

import com.voxyl.overlay.business.stats.apis.ApiProvider
import com.voxyl.overlay.business.settings.config.Columns
import com.voxyl.overlay.business.settings.config.Config
import com.voxyl.overlay.business.settings.config.HypixelApiKey
import com.voxyl.overlay.business.validation.popups.Error
import com.voxyl.overlay.controllers.common.PopUpQueue
import com.voxyl.overlay.controllers.common.CurrentScreen
import kotlinx.coroutines.*

@OptIn(DelicateCoroutinesApi::class)
object HypixelApiKeyValidator {
    private const val TAG = "HypixelApiKeyValidationPopup"

    init {
        CurrentScreen.subscribeToChange { _, new ->
            if (new == CurrentScreen.PlayerStats) {
                validate()
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun validate() {
        if (Config[Columns].contains("hypixel") || Config[Columns].contains("bedwars")) {
            GlobalScope.launch(Dispatchers.IO) {
                PopUpQueue.filter(TAG)

                val validity = isValid()

                if (validity == null) {
                    PopUpQueue.add(Error("You have Hypixel columns, but the Hypixel API may be down, or your key may be invalid").withTags(TAG))
                }

                if (validity == false) {
                    PopUpQueue.add(Error("You have Hypixel columns, but your Hypixel API key is invalid!").withTags(TAG))
                }
            }
        }
    }

    suspend fun isValid(): Boolean? {
        if (Config[HypixelApiKey].isEmpty()) {
            return false
        }

        val response = ApiProvider.hypixel
            .getKeyInfo(Config[HypixelApiKey])

        if (response.isSuccessful) {
            return true
        }

        return if (response.code() == 403) false else null
    }
}
