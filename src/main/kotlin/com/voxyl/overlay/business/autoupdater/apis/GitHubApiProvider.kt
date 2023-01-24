package com.voxyl.overlay.business.autoupdater.apis

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object GitHubApiProvider {
    fun getApi(): GitHubApi =
        Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GitHubApi::class.java)

    fun getDownloader(): GitHubDownloader =
        Retrofit.Builder()
            .baseUrl("https://github.com/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
            .create(GitHubDownloader::class.java)
}
