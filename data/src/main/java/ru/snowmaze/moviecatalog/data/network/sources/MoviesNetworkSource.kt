package ru.snowmaze.moviecatalog.data.network.sources

import ru.snowmaze.moviecatalog.data.network.MovieNetworkEntity

interface MoviesNetworkSource {

    suspend fun discoverMovies(
        page: Int,
        language: String
    ): List<MovieNetworkEntity>

    suspend fun searchMovies(
        searchQuery: String,
        page: Int,
        language: String
    ): List<MovieNetworkEntity>
}