package com.pthw.mymovieapp.data.mapper

import com.pthw.mymovieapp.data.network.response.MovieResponse
import com.pthw.mymovieapp.utils.DataMetaResponse
import com.pthw.mymovieapp.utils.UniMapper
import com.pthw.mymovieapp.utils.toMetaVO
import com.pthw.mymovieapp.vos.MetaVO
import com.pthw.mymovieapp.vos.MovieVO
import javax.inject.Inject

class PopularMoiveResponseMapper @Inject constructor() :
    UniMapper<DataMetaResponse<MovieResponse>?, MetaVO<MovieVO>> {
    override suspend fun map(data: DataMetaResponse<MovieResponse>?): MetaVO<MovieVO> {
        val list = data?.data?.map {
            MovieVO(
                id = it.id?: 0,
                adult = it.adult?: false,
                backdropPath = it.backdropPath.orEmpty(),
                movieTitle = it.movieTitle.orEmpty(),
                overview = it.overview.orEmpty(),
                popularity = it.popularity?: 0.0,
                posterPath = it.posterPath.orEmpty(),
                releaseDate = it.releaseDate.orEmpty(),
                voteAverage = it.voteAverage?: 0.0,
                voteCount = it.voteCount?: 0,
            )
        }.orEmpty()


        return list.toMetaVO(currentPage = data?.currentPage, totalPage = data?.totalPage)
    }
}