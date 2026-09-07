package com.michael.playlistmaker.domain.search.impl

import com.michael.playlistmaker.domain.search.api.TracksInteractor
import com.michael.playlistmaker.domain.search.api.TracksRepository
import com.michael.playlistmaker.domain.search.models.Track
import com.michael.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.concurrent.Executors

class TracksInteractorImpl(private val repository: TracksRepository) : TracksInteractor {
    //private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(expression: String): Flow<Pair<List<Track>?, String?>> {


        return repository.searchTracks(expression).map { result ->
            when (result) {
                is Resource.Success -> {
                    Pair(result.data, null)
                }

                is Resource.Error -> {
                    Pair(null, result.message)
                }
            }
            /*
            when (val resource = repository.searchTracks(expression)) {
                is Resource.Success -> {
                    consumer.consume(resource.data, null)
                }

                is Resource.Error -> {
                    consumer.consume(null, resource.message)
                }
                // consumer.consume(repository.searchTracks(expression))
            }

         */

        }
    }
}