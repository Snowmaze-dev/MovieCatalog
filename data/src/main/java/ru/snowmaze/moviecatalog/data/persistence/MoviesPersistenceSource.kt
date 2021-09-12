package ru.snowmaze.moviecatalog.data.persistence

interface MoviesPersistenceSource {

    suspend fun getAllFavorites(): List<MoviePersistenceEntity>

    suspend fun addFavorite(movie: MoviePersistenceEntity)

    suspend fun removeFavorite(movie: MoviePersistenceEntity)

}