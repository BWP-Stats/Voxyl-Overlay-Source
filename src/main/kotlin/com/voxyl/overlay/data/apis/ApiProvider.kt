package com.voxyl.overlay.data.apis

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

    fun getUUIDApi(): UUIDApi =
        Retrofit.Builder()
            .baseUrl("https://voxyl-api.herokuapp.com")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
            .create(UUIDApi::class.java)
}