package com.pthw.mymovieapp.data.repository

import com.pthw.mymovieapp.data.network.response.ErrorResponse
import com.pthw.mymovieapp.utils.Either
import com.pthw.mymovieapp.vos.HomeMovieVO
import com.pthw.mymovieapp.vos.MetaVO
import com.pthw.mymovieapp.vos.MovieVO

interface MovieRepository {
    suspend fun fetchHomeMovie(page: Int): Either<ErrorResponse, HomeMovieVO?>
    suspend fun getHomeMovie(): HomeMovieVO?

    suspend fun getPopularMovieList(page: Int): Either<ErrorResponse, MetaVO<MovieVO>>
    suspend fun getTopRatedMovieList(page: Int): Either<ErrorResponse, MetaVO<MovieVO>>
    suspend fun getUpComingMovieList(page: Int): Either<ErrorResponse, MetaVO<MovieVO>>

    suspend fun getSearchMovieList(page: Int, query: String): Either<ErrorResponse, MetaVO<MovieVO>>
}