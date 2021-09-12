package ru.snowmaze.moviecatalog.data

import ru.snowmaze.moviecatalog.data.network.MovieNetworkEntity
import ru.snowmaze.moviecatalog.data.persistence.MoviePersistenceEntity
import ru.snowmaze.moviecatalog.domain.Movie

fun MovieNetworkEntity.toDomainModel(isFavorite: Boolean) = Movie(
    id = id ?: 0,
    title = title ?: "",
    overview = overview,
    releaseDate = releaseDate ?: "",
    posterPath = BuildConfig.IMAGES_BASE_URL + posterPath,
    isFavorite = isFavorite
)

fun Movie.toPersistenceEntity() = MoviePersistenceEntity(
    id = id,
    title = title,
    overview = overview,
    releaseDate = releaseDate,
    posterPath = posterPath,
    isFavorite = isFavorite
)