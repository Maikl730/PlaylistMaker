package com.michael.playlistmaker.data.search

import com.michael.playlistmaker.data.search.dto.Response

interface NetworkClient {
    fun doRequest(dto: Any): Response

}