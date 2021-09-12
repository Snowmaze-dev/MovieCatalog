package ru.snowmaze.moviecatalog.data.persistence

import androidx.room.Entity

@Entity(primaryKeys = ["id"])
class MoviePersistenceEntity(
    val id: Int,
    val title: String,
    val posterPath: String?,
    val overview: String?,
    val releaseDate: String,
    val isFavorite: Boolean = false
)