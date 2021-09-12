package ru.snowmaze.moviecatalog.domain

data class Movie(
    val id: Int,
    val title: String,
    val posterPath: String?,
    val overview: String?,
    val releaseDate: String,
    val isFavorite: Boolean = false
)