package com.pthw.mymovieapp.vos

import java.io.Serializable

data class MovieVO(
    val adult: Boolean,
    val backdropPath: String,
    val id: Int,
    val movieTitle: String,
    val overview: String,
    val popularity: Double,
    val posterPath: String,
    val releaseDate: String,
    val voteAverage: Double,
    val voteCount: Int,

): Serializable
