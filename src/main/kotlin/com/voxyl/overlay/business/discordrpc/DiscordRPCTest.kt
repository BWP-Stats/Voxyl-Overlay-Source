package com.voxyl.overlay.business.discordrpc

import dev.cbyrne.kdiscordipc.KDiscordIPC

object DiscordRPCTest {
    fun `try`() {
        val ipc = KDiscordIPC("YOUR_CLIENT_ID")



        ipc.connect()
    }
}