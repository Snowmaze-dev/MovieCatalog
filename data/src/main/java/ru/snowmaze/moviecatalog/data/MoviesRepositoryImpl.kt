package ru.snowmaze.moviecatalog.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.snowmaze.moviecatalog.data.network.sources.MoviesNetworkSource
import ru.snowmaze.moviecatalog.data.persistence.MoviesPersistenceSource
import ru.snowmaze.moviecatalog.domain.Movie
import ru.snowmaze.moviecatalog.domain.MoviesRepository

class MoviesRepositoryImpl(
    private val networkSource: MoviesNetworkSource,
    private val persistenceSource: MoviesPersistenceSource
) : MoviesRepository {

    override suspend fun discoverMovies(
        searchQuery: String?,
        page: Int,
        language: String
    ): List<Movie> {
        val result = if (searchQuery == null) {
            networkSource.discoverMovies(page, language)
        } else {
            networkSource.searchMovies(searchQuery, page, language)
        }
        return withContext(Dispatchers.IO) {
            val favorites = persistenceSource.getAllFavorites()
            result.map { networkEntity ->
                networkEntity.toDomainModel(isFavorite = favorites.find {
                    networkEntity.id == it.id
                } != null)
            }
        }
    }

    override suspend fun addFavorite(movie: Movie) {
        persistenceSource.addFavorite(movie.toPersistenceEntity())
    }

    override suspend fun removeFavorite(movie: Movie) {
        persistenceSource.removeFavorite(movie.toPersistenceEntity())
    }
}