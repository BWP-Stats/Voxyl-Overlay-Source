package com.voxyl.overlay.data.apis

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import kotlin.system.measureTimeMillis

object ApiProvider {
    fun getBWPApi(): BWPApi =
        Retrofit.Builder()
            .baseUrl("https://api.voxyl.net")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BWPApi::class.java)

    fun getUUIDApi(): UUIDApi =
        Retrofit.Builder()
            .baseUrl("https://api.mojang.com/users/profiles/minecraft/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
            .create(UUIDApi::class.java)
}