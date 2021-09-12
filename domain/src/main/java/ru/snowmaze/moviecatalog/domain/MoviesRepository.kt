package ru.snowmaze.moviecatalog.domain

interface MoviesRepository {

    suspend fun discoverMovies(
        searchQuery: String? = null,
        page: Int = 1,
        language: String
    ): List<Movie>

    suspend fun addFavorite(movie: Movie)

    suspend fun removeFavorite(movie: Movie)

}