package com.michael.playlistmaker.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.michael.playlistmaker.data.NetworkClient
import com.michael.playlistmaker.data.dto.Response
import com.michael.playlistmaker.data.dto.TrackSearchRequest

import java.io.IOException

class RetrofitNetworkClient(private val itunesService:ItunesApiService,private val context: Context):NetworkClient {

    override fun doRequest(dto: Any): Response {
        if (isConnected() == false) {
            return Response().apply { resultCode = -1 }
        }
        if (dto !is TrackSearchRequest) {
            return Response().apply { resultCode = 400 }
        }

        val resp = itunesService.search(dto.expression).execute()
        val body = resp.body() ?: Response()

        return if (body!=null){
            body.apply { resultCode = resp.code()}
        }else{
            Response().apply { resultCode = resp.code() }
            }


        /*
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
         */
    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
}