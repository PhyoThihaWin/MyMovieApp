package com.pthw.mymovieapp.data.network.response

import com.squareup.moshi.Json

data class MovieResponse(
    @field:Json(name = "adult")
    val adult: Boolean?,

    @field:Json(name = "backdrop_path")
    val backdropPath: String?,

    @field:Json(name = "id")
    val id: Int?,

    @field:Json(name = "title")
    val movieTitle: String?,

    @field:Json(name = "overview")
    val overview: String?,

    @field:Json(name = "popularity")
    val popularity: Double?,

    @field:Json(name = "poster_path")
    val posterPath: String?,

    @field:Json(name = "release_date")
    val releaseDate: String?,

    @field:Json(name = "vote_average")
    val voteAverage: Double?,

    @field:Json(name = "vote_count")
    val voteCount: Int?,

)
