package com.pthw.mymovieapp.data.network.service

import com.pthw.mymovieapp.data.network.response.MovieResponse
import com.pthw.mymovieapp.utils.DataMetaResponse
import com.pthw.mymovieapp.utils.DataResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieService {

    @GET("movie/popular")
    suspend fun getPopularMovieList(
        @Query("page") page: Int
    ): Response<DataMetaResponse<MovieResponse>>

    @GET("movie/top_rated")
    suspend fun getTopRatedMovieList(
        @Query("page") page: Int
    ): Response<DataMetaResponse<MovieResponse>>

    @GET("movie/upcoming")
    suspend fun getUpcomingMovieList(
        @Query("page") page: Int
    ): Response<DataMetaResponse<MovieResponse>>

    @GET("search/movie")
    suspend fun getSearchMovieList(
        @Query("page") page: Int,
        @Query("query") query: String
    ): Response<DataMetaResponse<MovieResponse>>

}