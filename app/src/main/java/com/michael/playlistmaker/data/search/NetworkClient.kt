package com.michael.playlistmaker.data.search

import com.michael.playlistmaker.data.search.dto.Response

interface NetworkClient {
    suspend fun doRequest(dto: Any): Response

}