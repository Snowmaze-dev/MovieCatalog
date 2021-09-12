package ru.snowmaze.moviecatalog.data.network

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class MoviesResponse(val page: Int, val results: List<MovieNetworkEntity>)