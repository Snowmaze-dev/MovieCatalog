package ru.snowmaze.moviecatalog.data.network.sources

import ru.snowmaze.moviecatalog.data.BuildConfig
import ru.snowmaze.moviecatalog.data.network.MovieNetworkEntity
import ru.snowmaze.moviecatalog.data.network.MoviesAPIService
import javax.inject.Inject

class MoviesNetworkSourceImpl(
    private val apiService: MoviesAPIService
) : MoviesNetworkSource {

    override suspend fun discoverMovies(
        page: Int,
        language: String
    ): List<MovieNetworkEntity> {
        return apiService.discoverMovies(
            apiKey = BuildConfig.API_KEY,
            page = page,
            language = language
        ).results
    }

    override suspend fun searchMovies(
        searchQuery: String,
        page: Int,
        language: String
    ): List<MovieNetworkEntity> {
        return apiService.searchMovies(
            query = searchQuery,
            apiKey = BuildConfig.API_KEY,
            page = page,
            language = language
        ).results
    }

}