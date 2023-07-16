package com.voxyl.overlay.controllers.common

import androidx.compose.runtime.toMutableStateList
import com.voxyl.overlay.business.settings.config.PlayerAliases
import com.voxyl.overlay.business.settings.config.Config
import com.voxyl.overlay.business.settings.config.PlayerName
import com.voxyl.overlay.business.validation.popups.Info

object Aliases {
    val _aliases = Config[PlayerAliases].split(",").filter { it != "" }.toMutableStateList()

    val aliases: List<String>
        get() = _aliases.toList()

    fun isAlias(name: String): Boolean {
        return Config[PlayerName].equals(name, true) || isStrictlyAlias(name)
    }

    fun isStrictlyAlias(name: String): Boolean {
        return aliases.any { it.trim('*').equals(name, true) }
    }

    fun save() {
        Config[PlayerAliases] = _aliases.joinToString(",")
    }

    private val validNamePattern = "\\w{1,16}".toRegex()

    fun add(name: String) {
        if (!name.matches(validNamePattern))
            return

        if (!_aliases.contains(name)) {
            _aliases.add(name)
        }
        save()
    }

    fun addNick(name: String) {
        if (!name.matches(validNamePattern))
            return

        val prevNick = _aliases.find { it.startsWith('*') }

        _aliases.add("*$name")

        if (prevNick != null) {
            _aliases.remove(prevNick)
            PopUpQueue.add(Info("Added '$name' as a new alias and removed the old auto-added alias '${prevNick.trim('*')}'"))
        } else {
            PopUpQueue.add(Info("Added '$name' as a new alias"))
        }
    }

    fun remove(name: String) {
        _aliases.remove(name)
        save()
    }
}
