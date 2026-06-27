package com.michael.playlistmaker.data.dto

import com.michael.playlistmaker.domain.models.Track

class SongResponse(val results:List<TrackDto>):Response() {
}