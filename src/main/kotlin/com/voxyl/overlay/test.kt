@file:OptIn(DelicateCoroutinesApi::class)

package com.voxyl.overlay

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.application
import kotlinx.coroutines.*

fun main() = application {

    /*val stats: Stats? = null
    val sumo: Sumo? = null

    val (wins, finals) = sumo!!

    for (prop in Stats::class.properties) {
        println("${prop.name} = ${prop.get(stats!!)}")
    }*/

    //ApiProvider.getBWPApi().getGameStats("c62d2b59-bf09-4517-a059-0925fac113d6", "HhtTKOr5nIvl8adDZMtaLAjsBhClrmvp")
}


@Composable
fun HelloScreen() {
    var name by rememberSaveable { mutableStateOf("") }

    HelloContent(name = name, onNameChange = { name = it })
}

@Composable
fun HelloContent(name: String, onNameChange: (String) -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Hello, $name",
            modifier = Modifier.padding(bottom = 8.dp),
            style = MaterialTheme.typography.h5
        )
        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            label = { Text("Name") }
        )
    }
}