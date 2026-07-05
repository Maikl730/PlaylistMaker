package com.michael.playlistmaker.data.network

import android.util.Log
import com.michael.playlistmaker.data.NetworkClient
import com.michael.playlistmaker.data.dto.Response
import com.michael.playlistmaker.data.dto.SongResponse
import com.michael.playlistmaker.data.dto.TrackSearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class RetrofitNetworkClient:NetworkClient {
    private val itunesBaseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(itunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val itunesService = retrofit.create(ItunesApiService::class.java)

    override fun doRequest(dto: Any): Response {
        if (dto is TrackSearchRequest) {
            try{
                 val resp = itunesService.search(dto.expression).execute()
                 val body = resp.body() ?: Response()

                return body.apply { resultCode = resp.code() }

            }catch (e: IOException){
                return Response().apply { resultCode = 500 }
            }
        } else {
            return Response().apply { resultCode = 400 }
        }
    }
}