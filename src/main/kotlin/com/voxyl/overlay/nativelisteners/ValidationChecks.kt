package com.voxyl.overlay.nativelisteners

import com.voxyl.overlay.dataslashbusiness.events.Tip
import com.voxyl.overlay.dataslashbusiness.events.Error
import com.voxyl.overlay.dataslashbusiness.events.Warning
import com.voxyl.overlay.dataslashbusiness.validation.BwpApiValidator
import com.voxyl.overlay.dataslashbusiness.validation.HypixelApiKeyValidator
import com.voxyl.overlay.kindasortasomewhatviewmodelsishiguessithinkidkwhatevericantbebotheredsmh.EventsToBeDisplayed
import com.voxyl.overlay.settings.config.Config
import com.voxyl.overlay.settings.config.ConfigKeys.*
import com.voxyl.overlay.settings.misc.Misc
import com.voxyl.overlay.settings.misc.MiscKeys.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

object ValidationChecks {
    fun runAtStart(cs: CoroutineScope) {
        var launch = true

        if (Config[BwpApiKey] == "" && Config[HypixelApiKey] == "" && Config[LogFilePath] == "" && Config[PlayerName] == "") {
            EventsToBeDisplayed.add(Tip("Click on the top-left button to set up the overlay!", 10000))
            launch = false
        }

        if (Misc[FirstTime] != "false") {
            EventsToBeDisplayed.add(Tip("You can hover over most of the buttons/icons/tags to see what they do/mean!", 10000))
            EventsToBeDisplayed.add(Tip("You can also right click on added players for more options!", 10000))
            Misc[FirstTime] = "false"
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
            EventsToBeDisplayed.add(Error("Your API keys are invalid! Please check them and try again!"))
            return
        }

        if (bwp == null) {
            EventsToBeDisplayed.add(Warning("The BWP api may be down, or your key may be invalid"))
        }

        if (bwp == false) {
            EventsToBeDisplayed.add(Error("Please fill in your BWP api key!"))
        }

        if (hypixel == null) {
            EventsToBeDisplayed.add(Warning("The Hypixel api may be down, or your key may be invalid"))
        }

        if (hypixel == false) {
            EventsToBeDisplayed.add(Error("Your Hypixel api key is invalid!"))
        }
    }
}