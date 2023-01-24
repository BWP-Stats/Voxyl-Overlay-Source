package com.voxyl.overlay.business.validation

import com.voxyl.overlay.business.settings.config.*
import com.voxyl.overlay.business.settings.misc.FirstTimeUsingOverlay
import com.voxyl.overlay.business.settings.misc.MiscSettings
import com.voxyl.overlay.business.validation.popups.Tip
import com.voxyl.overlay.controllers.common.PopUpQueue

object ValidationChecks {
    fun runAtStart() {
        var launch = true

        if (Config[BwpApiKey] == "" && Config[HypixelApiKey] == "" && Config[LogFilePath] == "" && Config[PlayerName] == "") {
            PopUpQueue.add(Tip("Click on the top-left button to set up the overlay!", 10000))
            launch = false
        }

        if (MiscSettings[FirstTimeUsingOverlay] != "false") {
            PopUpQueue.add(Tip("You can hover over most of the buttons/icons/tags to see what they do/mean!", 10000))
            PopUpQueue.add(Tip("You can also right click on added players for more options!", 10000))
        }

        if (!launch) return

        HypixelApiKeyValidator.validate()
        BwpApiKeyValidator.validate()
    }
}
