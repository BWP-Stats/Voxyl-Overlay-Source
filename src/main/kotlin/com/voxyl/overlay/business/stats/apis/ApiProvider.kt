package com.voxyl.overlay.business.stats.apis

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object ApiProvider {
    val bwp: BwpApi =
        Retrofit.Builder()
            .baseUrl("https://api.voxyl.net")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ActualBwpApi::class.java)

    val hypixel: HypixelApi =
        Retrofit.Builder()
            .baseUrl("https://api.hypixel.net")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(HypixelApi::class.java)

    val mojang: MojangApi =
        Retrofit.Builder()
            .baseUrl("https://api.mojang.com/users/profiles/minecraft/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MojangApi::class.java)
}
