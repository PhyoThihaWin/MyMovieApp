package com.pthw.mymovieapp.data.repository

import com.pthw.mymovieapp.data.local.db.AppDatabase
import com.pthw.mymovieapp.data.mapper.HomeMovieResponseMapper
import com.pthw.mymovieapp.data.mapper.PopularMoiveResponseMapper
import com.pthw.mymovieapp.data.network.response.ErrorResponse
import com.pthw.mymovieapp.data.network.service.MovieService
import com.pthw.mymovieapp.utils.Either
import com.pthw.mymovieapp.utils.handleForMeta
import com.pthw.mymovieapp.utils.handleForZip
import com.pthw.mymovieapp.utils.zip
import com.pthw.mymovieapp.vos.HomeMovieVO
import com.pthw.mymovieapp.vos.MetaVO
import com.pthw.mymovieapp.vos.MovieVO
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val service: MovieService,
    private val homeMapper: HomeMovieResponseMapper,
    private val listMapper: PopularMoiveResponseMapper,
    private val appDatabase: AppDatabase
): MovieRepository {

    override suspend fun fetchHomeMovie(page: Int): Either<ErrorResponse, HomeMovieVO?> {
        return handleForZip(
            apiCall = {
                val flow1 = flowOf(service.getPopularMovieList(page))
                val flow2 = flowOf(service.getTopRatedMovieList(page))
                val flow3 = flowOf(service.getUpcomingMovieList(page))

                zip(flow1, flow2, flow3) { a, b, c ->
                    Triple(a,b,c)
                }

            },
            handler = {
                it?.let {
                    appDatabase.getHomeMovieDao().deleteAll()
                    appDatabase.getHomeMovieDao().insert(HomeMovieVO(
                        trendingList = homeMapper.map(it.first.data),
                        topRatedList = homeMapper.map(it.second.data),
                        upcomingList = homeMapper.map(it.third.data)))
                }
                Either.Right(appDatabase.getHomeMovieDao().getHomeMovie())
            }
        )
    }

    override suspend fun getHomeMovie(): HomeMovieVO? {
        return appDatabase.getHomeMovieDao().getHomeMovie()
    }

    override suspend fun getPopularMovieList(page: Int): Either<ErrorResponse, MetaVO<MovieVO>> {
        return handleForMeta(
            apiCall = {
                service.getPopularMovieList(page = page)
            },
            handler = {
                Either.Right(listMapper.map(it))
            }
        )
    }

    override suspend fun getTopRatedMovieList(page: Int): Either<ErrorResponse, MetaVO<MovieVO>> {
        return handleForMeta(
            apiCall = {
                service.getTopRatedMovieList(page = page)
            },
            handler = {
                Either.Right(listMapper.map(it))
            }
        )
    }

    override suspend fun getUpComingMovieList(page: Int): Either<ErrorResponse, MetaVO<MovieVO>> {
        return handleForMeta(
            apiCall = {
                service.getUpcomingMovieList(page = page)
            },
            handler = {
                Either.Right(listMapper.map(it))
            }
        )
    }

    override suspend fun getSearchMovieList(page: Int, query: String): Either<ErrorResponse, MetaVO<MovieVO>> {
        return handleForMeta(
            apiCall = {
                service.getSearchMovieList(page = page, query = query)
            },
            handler = {
                Either.Right(listMapper.map(it))
            }
        )
    }
}