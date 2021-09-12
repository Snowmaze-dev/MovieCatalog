package ru.snowmaze.moviecatalog.data.network

import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesAPIService {

    @GET("discover/movie")
    suspend fun discoverMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): MoviesResponse

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): MoviesResponse

}