package com.pthw.mymovieapp.mvvm.event

import com.pthw.mymovieapp.vos.MovieVO

sealed class BrowseUiEvent {
    data class Error(val message: String) : BrowseUiEvent()
    data class SuccessMovieList(val item: List<MovieVO>) : BrowseUiEvent()
}
