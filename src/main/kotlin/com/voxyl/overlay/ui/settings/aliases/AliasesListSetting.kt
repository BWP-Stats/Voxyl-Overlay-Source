package com.voxyl.overlay.ui.settings.aliases

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.voxyl.overlay.business.settings.config.Config
import com.voxyl.overlay.business.settings.config.PlayerName
import com.voxyl.overlay.controllers.common.Aliases
import com.voxyl.overlay.controllers.common.ui.am
import com.voxyl.overlay.ui.elements.VText
import com.voxyl.overlay.ui.elements.VTrailingIcon
import com.voxyl.overlay.ui.settings.SettingsTextField

@ExperimentalComposeUiApi
@Composable
fun AliasesTextField() {
    var alias by remember { mutableStateOf(TextFieldValue()) }

    val doOnEnter = doOnEnter@{
        if (!isValidName(alias)) return@doOnEnter

        Aliases.add(alias.text)

        alias = TextFieldValue()
    }

    SettingsTextField(
        text = getAliasLabel(alias, isValidName(alias)),
        placeholder = "Aliases (Nicknames, Alts) who are you",
        value = alias,
        onValueChange = { alias = it },
        doOnEnter = doOnEnter,
        isValid = { alias.text.isBlank() || isValidName(it) },
        modifier = Modifier.offset(y = -10.dp),
        trailingIcon = {
            Row {
                VTrailingIcon(
                    modifier = Modifier
                        .offset(y = 10.dp)
                        .size(12.dp, 12.dp)
                ) {
                    if (isValidName(alias)) doOnEnter()
                }
                Spacer(modifier = Modifier.size(3.dp))
                Row(
                    modifier = Modifier
                        .offset(y = 5.dp)
                        .widthIn(max = 300.dp)
                        .horizontalScroll(rememberScrollState())
                ) {
                    Aliases.aliases.forEach {
                        AliasTag(it)
                    }
                }
            }
        }
    )
}

@Composable
private fun AliasTag(alias: String) {
    VText(
        text = " $alias ",
        modifier = Modifier.padding(horizontal = 3.dp)
            .background(Color(.2f, .2f, .2f, .7f).am)
            .clickable(alias.lowercase() != Config[PlayerName].lowercase()) {
                Aliases.remove(alias)
            },
        fontSize = TextUnit.Unspecified
    )
}

private fun getAliasLabel(apiKey: TextFieldValue, isValid: Boolean) =
    if (apiKey.text.isNotBlank() && !isValid)
        "Please enter a valid name"
    else if (apiKey.text.contains(" "))
        "Press enter to enter the alias (without any spaces)"
    else
        "Enter your aliases (Nicknames, Alts)"

private fun isValidName(tfv: TextFieldValue) = tfv.text.matches(Regex("\\w{1,16}"))
