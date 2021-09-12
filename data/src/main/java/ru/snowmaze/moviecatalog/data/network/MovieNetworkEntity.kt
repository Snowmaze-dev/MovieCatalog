package ru.snowmaze.moviecatalog.data.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class MovieNetworkEntity(
    val id: Int?,
    val title: String?,
    val overview: String? = "",
    @Json(name = "release_date") val releaseDate: String? = "",
    @Json(name = "poster_path") val posterPath: String? = ""
)