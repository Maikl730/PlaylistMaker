package com.michael.playlistmaker.data.network

import android.util.Log
import com.michael.playlistmaker.data.NetworkClient
import com.michael.playlistmaker.data.dto.Response
import com.michael.playlistmaker.data.dto.TrackSearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient:NetworkClient {
    private val itunesBaseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(itunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val itunesService = retrofit.create(ItunesApiService::class.java)

    override fun doRequest(dto: Any): Response {
        if (dto is TrackSearchRequest) {
            val resp = itunesService.search(dto.expression).execute()

            Log.d("MyLog",resp.toString() + "CODE retrofitnetworkclient")
            val body = resp.body() ?: Response()
            Log.d("MyLog",body.resultCode.toString() + "CODE retrofitnetworkclient")

            return body.apply { resultCode = resp.code() }
        } else {
            return Response().apply { resultCode = 400 }
        }
    }
}