package com.voxyl.overlay.business.validation

import com.voxyl.overlay.business.utils.NetworkingUtils
import com.voxyl.overlay.business.stats.apis.ApiProvider
import com.voxyl.overlay.business.settings.config.BwpApiKey
import com.voxyl.overlay.business.settings.config.Config
import com.voxyl.overlay.business.validation.popups.Error
import com.voxyl.overlay.controllers.common.PopUpQueue
import com.voxyl.overlay.controllers.common.CurrentScreen
import kotlinx.coroutines.*

object BwpApiKeyValidator {
    private const val TAG = "BwpApiKeyValidationPopup"

    init {
        CurrentScreen.subscribeToChange { _, new ->
            if (new == CurrentScreen.PlayerStats) {
                validate()
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun validate() = GlobalScope.launch(Dispatchers.IO) {
        PopUpQueue.filter(TAG)

        val validity = isValid()

        if (validity != true) {
            PopUpQueue.add(Error("Your BWP API key is invalid! (or the API is down (typical tom smh))").withTags(TAG))
        }
    }

    suspend fun isValid(): Boolean? {
        if (Config[BwpApiKey].isEmpty()) {
            return false
        }

        val response = ApiProvider.bwp
            .getPlayerInfo("e21d44c5-c1fd-4119-b55c-5baced12fd6e", Config[BwpApiKey])

        if (response.isSuccessful) {
            return true
        }

        val errorReasonIsApiKey = NetworkingUtils
            .stringifyError(response)
            ?.get("reason")
            ?.asString
            ?.contains("api key", true)

        return if (errorReasonIsApiKey == true) false else null
    }
}
