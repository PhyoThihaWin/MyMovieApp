package com.pthw.mymovieapp.mvvm.event

import com.pthw.mymovieapp.vos.HomeMovieVO
import com.pthw.mymovieapp.vos.MovieVO

sealed class HomeUiEvent {
    data class Error(val message: String) : HomeUiEvent()
    data class SuccessHomeMovie(val item: HomeMovieVO) : HomeUiEvent()

    data class SuccessMovieList(val item: List<MovieVO>) : HomeUiEvent()
}
