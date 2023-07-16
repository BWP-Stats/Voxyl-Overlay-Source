package com.voxyl.overlay.ui.settings.regex

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.voxyl.overlay.business.settings.config.Config
import com.voxyl.overlay.business.settings.config.CustomRegex
import com.voxyl.overlay.business.utils.StateRotator
import com.voxyl.overlay.business.utils.rememberComposeStateRotatorOf
import com.voxyl.overlay.controllers.common.ui.MainWhite
import com.voxyl.overlay.controllers.common.ui.am
import com.voxyl.overlay.ui.elements.VText
import com.voxyl.overlay.ui.elements.VTrailingIcon
import com.voxyl.overlay.ui.settings.SettingsTextField

const val REGEX_INTRA_SEPERATOR = "]sadf]op]@#[;!@"
const val REGEX_INTER_SEPERATOR = ".[.pl34$%f;l.;["

const val REGEX_ACTION_ADD = "ADD"
const val REGEX_ACTION_REMOVE = "REMOVE"

const val REGEX_MATCH_IN_ONLY_CHAT = "ONLY CHAT"
const val REGEX_MATCH_IN_A_FULL_LINE = "A FULL LINE"

const val REGEX_MATCH_TYPE_FULLY_MATCHES = "FULLY MATCHES"
const val REGEX_MATCH_TYPE_IS_FOUND_IN = "IS FOUND IN"

@Composable
@ExperimentalComposeUiApi
fun RegexTextField() {
    var regex by remember { mutableStateOf(TextFieldValue()) }

    val action = rememberComposeStateRotatorOf(REGEX_ACTION_ADD, REGEX_ACTION_REMOVE)
    val matchIn = rememberComposeStateRotatorOf(REGEX_MATCH_IN_ONLY_CHAT, REGEX_MATCH_IN_A_FULL_LINE)
    val matchType = rememberComposeStateRotatorOf(REGEX_MATCH_TYPE_FULLY_MATCHES, REGEX_MATCH_TYPE_IS_FOUND_IN)

    var regexes = remember {
        (Config[CustomRegex]
            .split(REGEX_INTRA_SEPERATOR)
            .filter { it != "" })
            .toMutableStateList() }

    val doOnEnter = doOnEnter@{
        if (regex.text.isBlank()) return@doOnEnter

        regexes += arrayOf(regex.text, action.get(), matchType.get(), matchIn.get()).joinToString(REGEX_INTER_SEPERATOR)

        regexes = regexes.distinct().toMutableStateList()

        Config[CustomRegex] = regexes.joinToString(REGEX_INTRA_SEPERATOR)

        regex = TextFieldValue()
    }

    VText("" +
        "You can enter however many regexes you want accompanied by the desired action to match names of players " +
        "that you want to add to the overlay. An example to match ranked players that join a lobby would be:" +
        "\n" +
        "\n" +
        "ADD when the below regex FULLY MATCHES ONLY CHAT ^\\[.+] (.*) has joined!" +
        "\n" +
        "\n" +
        "The regex will add to the overlay whatever is matched by capture groups, and lists of players separated by " +
        "either spaces, or by commas followed by a space" +
        "\n" +
        "\n" +
        "I'll make this more flexible in the future. Ping me for explanations if you're already familiar with regex " +
        "(I'm not teaching regex here, sorry). Forgive me if this is not coherent, I'm sick with the flu rn as I'm " +
        "writing this." +
        "\n" +
        "\n" +
        "(Shift + scroll through your regexes if you have a lot)" +
        "\n",
        modifier = Modifier
            .padding(horizontal = 20.dp)
    )

    Row(
        modifier = Modifier
            .offset(y = -2.5.dp)
            .padding(horizontal = 20.dp, vertical = 5.dp)
            .height(30.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        StateSelectionBox(action, 95.dp)

        Spacer(Modifier.width(5.dp))

        VText("when the below regex", fontWeight = FontWeight.Bold)

        Spacer(Modifier.width(5.dp))

        StateSelectionBox(matchType, 145.dp)

        Spacer(Modifier.width(5.dp))

        StateSelectionBox(matchIn, 115.dp)
    }

    SettingsTextField(
        text = "Enter some custom regex.",
        placeholder = "(Intended for more advanced uses, but Regex isn't too hard to learn the basics)",
        value = regex,
        onValueChange = { regex = it },
        doOnEnter = doOnEnter,
        modifier = Modifier.offset(y = -10.dp),
        trailingIcon = {
            VTrailingIcon(
                modifier = Modifier
                    .offset(y = 10.dp)
                    .size(12.dp, 12.dp)
            ) { doOnEnter() }
        }
    )

    Row(
        modifier = Modifier
            .offset(y = 5.dp)
            .padding(horizontal = 20.dp)
            .horizontalScroll(rememberScrollState())
    ) {
        regexes.forEach {
            AliasTag(it, regexes)
        }
    }

    Spacer(Modifier.height(5.dp))
}

@Composable
private fun AliasTag(_regex: String, regexes: SnapshotStateList<String>) {
    val (regex, action, matchIn, matchType) = _regex.split(REGEX_INTER_SEPERATOR)

    VText(
        text = " $action when /$regex/ $matchIn $matchType ",
        modifier = Modifier.padding(horizontal = 3.dp)
            .background(Color(.2f, .2f, .2f, .7f).am)
            .clickable {
                regexes.remove(_regex)
            },
        fontSize = TextUnit.Unspecified
    )
}

@Composable
private fun StateSelectionBox(stateRotator: StateRotator<String>, width: Dp) {
    Row(
        modifier = Modifier
            .background(Color(0f, 0f, 0f, .3f).am)
            .fillMaxHeight()
            .width(width)
            .clickable {
                stateRotator.rotate()
            },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        VText(
            text = stateRotator.get(),
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 10.dp)
        )

        Icon(
            Icons.Filled.ArrowDropDown,
            contentDescription = null,
            tint = MainWhite,
            modifier = Modifier
                .rotate(if (stateRotator.get() == stateRotator.first) 0f else 180f)
                .offset(y = if (stateRotator.get() == stateRotator.first) 1.2.dp else -1.2.dp)
        )
    }
}
