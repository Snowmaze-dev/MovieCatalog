package ru.snowmaze.moviecatalog.data.persistence

class MoviesPersistenceSourceImpl(private val dao: MovieDao) : MoviesPersistenceSource {
    override suspend fun getAllFavorites(): List<MoviePersistenceEntity> {
        return dao.getAllFavorites()
    }

    override suspend fun addFavorite(movie: MoviePersistenceEntity) {
        dao.insert(movie)
    }

    override suspend fun removeFavorite(movie: MoviePersistenceEntity) {
        dao.remove(movie)
    }
}