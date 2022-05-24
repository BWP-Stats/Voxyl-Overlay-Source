package com.voxyl.overlay.business.validation

import com.voxyl.overlay.business.validation.popups.Tip
import com.voxyl.overlay.business.validation.popups.Error
import com.voxyl.overlay.business.validation.popups.Warning
import com.voxyl.overlay.kindasortasomewhatviewmodelsishiguessithinkidkwhatevericantbebotheredsmh.PopUpQueue
import com.voxyl.overlay.settings.config.Config
import com.voxyl.overlay.settings.config.ConfigKeys.*
import com.voxyl.overlay.settings.misc.MiscSettings
import com.voxyl.overlay.settings.misc.MiscKeys.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

object ValidationChecks {
    fun runAtStart(cs: CoroutineScope) {
        var launch = true

        if (Config[BwpApiKey] == "" && Config[HypixelApiKey] == "" && Config[LogFilePath] == "" && Config[PlayerName] == "") {
            PopUpQueue.add(Tip("Click on the top-left button to set up the overlay!", 10000))
            launch = false
        }

        if (MiscSettings[FirstTime] != "false") {
            PopUpQueue.add(Tip("You can hover over most of the buttons/icons/tags to see what they do/mean!", 10000))
            PopUpQueue.add(Tip("You can also right click on added players for more options!", 10000))
            MiscSettings[FirstTime] = "false"
        }

        if (!launch) return

        cs.launch {
            validateApiKeys()
        }
    }

    suspend fun validateApiKeys() {
        val bwp = BwpApiValidator.validateApiKey()
        val hypixel = HypixelApiKeyValidator.validateApiKey()

        if (bwp == false && hypixel == false) {
            PopUpQueue.add(Error("Your API keys are invalid! Please check them and try again!"))
            return
        }

        if (bwp == null) {
            PopUpQueue.add(Warning("The BWP api may be down, or your key may be invalid"))
        }

        if (bwp == false) {
            PopUpQueue.add(Error("Please fill in your BWP api key!"))
        }

        if (hypixel == null) {
            PopUpQueue.add(Warning("The Hypixel api may be down, or your key may be invalid"))
        }

        if (hypixel == false) {
            PopUpQueue.add(Error("Your Hypixel api key is invalid!"))
        }
    }
}