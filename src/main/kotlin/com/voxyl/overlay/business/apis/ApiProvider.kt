package com.voxyl.overlay.business.apis

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object ApiProvider {
    fun getBWPApi(): BWPApi =
        Retrofit.Builder()
            .baseUrl("https://api.voxyl.net")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BWPApi::class.java)

    fun getHypixelApi(): HypixelApi =
        Retrofit.Builder()
            .baseUrl("https://api.hypixel.net")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(HypixelApi::class.java)

    fun getMojangApi(): MojangApi =
        Retrofit.Builder()
            .baseUrl("https://api.mojang.com/users/profiles/minecraft/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
            .create(MojangApi::class.java)
}