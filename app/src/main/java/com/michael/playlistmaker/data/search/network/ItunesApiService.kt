package com.michael.playlistmaker.data.search.network

import com.michael.playlistmaker.data.search.dto.SongResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesApiService {
    @GET("/search?entity=song")
    fun search(
        @Query("term") text: String
    ): Call<SongResponse>
}