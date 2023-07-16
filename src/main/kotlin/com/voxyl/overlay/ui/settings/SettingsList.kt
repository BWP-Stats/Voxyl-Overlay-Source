@file:OptIn(ExperimentalComposeUiApi::class)

package com.voxyl.overlay.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.Divider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.voxyl.overlay.business.settings.config.*
import com.voxyl.overlay.controllers.common.ui.MainWhite
import com.voxyl.overlay.controllers.common.ui.tbsm
import com.voxyl.overlay.ui.elements.*
import com.voxyl.overlay.ui.elements.util.requestFocusOnClick
import com.voxyl.overlay.ui.settings.aliases.AliasesTextField
import com.voxyl.overlay.ui.settings.aliases.ShowYourStatsInsteadOfAliasesCheckBox
import com.voxyl.overlay.ui.settings.appearances.*
import com.voxyl.overlay.ui.settings.backup.BackupButtons
import com.voxyl.overlay.ui.settings.basic.*
import com.voxyl.overlay.ui.settings.columns.ColumnsSettings
import com.voxyl.overlay.ui.settings.discordrp.ShowDiscordRPCheckbox
import com.voxyl.overlay.ui.settings.keybinds.ClearPlayersKeySetting
import com.voxyl.overlay.ui.settings.keybinds.OpenCloseKeySetting
import com.voxyl.overlay.ui.settings.keybinds.RefreshPlayersKeySetting
import com.voxyl.overlay.ui.settings.qol.*
import com.voxyl.overlay.ui.settings.regex.RegexTextField

@Composable
fun Settings(
    modifier: Modifier = Modifier
) {
    SettingsList(
        modifier = modifier,
        lazyListState = rememberLazyListState()
    )
}

@Composable
fun SettingsList(
    lazyListState: LazyListState,
    modifier: Modifier = Modifier
) {
    val addYourself = remember {
        mutableStateOf(Config[AddYourselfToOverlay] != "false")
    }

    val addBots = remember {
        mutableStateOf(Config[AddBotsToOverlay] != "false")
    }

    val autoHide = remember {
        mutableStateOf(Config[AutoShowAndHide] != "false")
    }

    val rawSettings by remember {
        mutableStateOf(
            listOf(
                @Composable { Header("Basic") } to "basicbwpapikeytextfieldhypixelapikeytextfieldplayernametextfieldusernamelogfilepathtextfieldbackupapikey",
                @Composable { BWPApiKeyTextField() } to "basicbwpapikeytextfield",
                @Composable { HypixelApiKeyTextField() } to "basichypixelapikeytextfield",
                @Composable { PlayerNameTextField() } to "basicplayernametextfieldusername",
                @Composable { LogFilePathTextField() } to "basiclogfilepathtextfield",
                @Composable { Spacer(modifier = Modifier.size(10.dp)) } to "nevergonnagiveyouupnevergonnaletyoudown",
                @Composable { UseBackupBwpApiCheckbox() } to "backupapikey",

                @Composable { Header("QOL") } to "qolpinyourselftotopcheckboxaddyourselftooverlaycheckboxautoshowandhidecheckboxautoshowandhidedelaysliderautohide",
                @Composable { PinYourselfToTopCheckbox(addYourself) } to "qolpinyourselftotopcheckbox",
                @Composable { AddYourselfToOverlayCheckbox(addYourself) } to "qoladdyourselftooverlaycheckbox",
                @Composable { AddBotsToOverlayCheckbox(addBots) } to "qoladdbotstooverlaycheckbox",
                @Composable { AutoShowAndHideCheckBox(autoHide) } to "qolautoshowandhidecheckboxautohide",
                @Composable { AutoShowAndHideDelaySlider(autoHide) } to "qolautoshowandhidedelaysliderautohide",

                @Composable { Header("Appearance") } to "appearanceopacityslidertitlebarsizeslidercenterstatscheckboxrslidergsliderbsliderredgreenblue",
                @Composable { BackgroundOpacitySlider() } to "appearancebackgroundopacityslider",
                @Composable { OpacitySlider() } to "appearanceopacityslider",
                @Composable { TitleBarSizeSlider() } to "appearancetitlebarsizeslider",
                @Composable { CenterStatsCheckBox() } to "appearancecenterstatscheckbox",
                @Composable { RSlider() } to "appearancersliderred",
                @Composable { GSlider() } to "appearancegslidergreen",
                @Composable { BSlider() } to "appearancebsliderblue",

                @Composable { Header("Keybinds") } to "keybindsopenclosekeysettingclearplayerskeysettinghotkeysrefreshkeybinds",
                @Composable { OpenCloseKeySetting() } to "keybindsopenclosekeysettinghotkeys",
                @Composable { ClearPlayersKeySetting() } to "keybindsclearplayerskeysettinghotkeys",
                @Composable { RefreshPlayersKeySetting() } to "keybindsrefreshplayerskeysettinghotkeys",

                @Composable { Header("Aliases (Shift + scroll to scroll through your aliases when many are present)") } to "aliasesnicksnicknamesaltaccounts",
                @Composable { ShowYourStatsInsteadOfAliasesCheckBox() } to "showyourstatsinsteadofaliasescheckbox",
                @Composable { AliasesTextField() } to "aliasesnicksnicknamesaltaccounts",

                @Composable { Header("Discord Rich Presence") } to "discordrichpresencediscordrpc",
                @Composable { ShowDiscordRPCheckbox() } to "discordrichpresencediscordrpc",

                @Composable { Header("Columns") } to "columnsstatsshowrows",
                @Composable { ColumnsSettings() } to "columnsstatsshowrows",

                @Composable { Header("Custom matching & actions") } to "custommatchingandactions&regex",
                @Composable { RegexTextField() } to "custommatchingandactions&regex",

                @Composable { Header("Backup") } to "backupsettingsfileconfig",
                @Composable { BackupButtons() } to "backupsettingsfileconfig",
            )
        )
    }

    val filteredSettings = if (queriedSetting.text.isEmpty()) {
        rawSettings
    } else {
        rawSettings.filter {
            it.second.contains(queriedSetting.text.replace(" ", ""), true)
        }
    }

    LazyColumn(
        state = lazyListState,
        modifier = modifier
            .absoluteOffset(y = 60.tbsm.dp)
            .scrollbar(lazyListState)
            .fillMaxSize()
            .requestFocusOnClick(),
        contentPadding = PaddingValues(bottom = 70.dp)
    ) {
        items(filteredSettings) {
            it.first()
        }
    }
}

@Composable
private fun Header(text: String) {
    Row(
        Modifier.fillMaxWidth().height(30.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Divider(modifier = Modifier.width(100.dp).absolutePadding(left = 20.dp), color = MainWhite)
        VText(text, modifier = Modifier.padding(5.dp))
        Divider(modifier = Modifier.weight(1f).absolutePadding(right = 20.dp), color = MainWhite)
    }
}
