package com.michael.playlistmaker.data

import com.michael.playlistmaker.data.dto.Response

interface NetworkClient {
    fun doRequest(dto: Any): Response

}