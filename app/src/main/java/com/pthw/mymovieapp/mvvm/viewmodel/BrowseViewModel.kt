package com.pthw.mymovieapp.mvvm.viewmodel

import androidx.lifecycle.viewModelScope
import androidx.room.Query
import com.pthw.mymovieapp.base.BaseViewModel
import com.pthw.mymovieapp.data.repository.MovieRepository
import com.pthw.mymovieapp.mvvm.event.BrowseUiEvent
import com.pthw.mymovieapp.mvvm.event.HomeUiEvent
import com.pthw.mymovieapp.utils.PageStageHandler
import com.pthw.mymovieapp.utils.isFirstPage
import com.pthw.mymovieapp.vos.MovieVO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BrowseViewModel @Inject constructor(
    private val repository: MovieRepository,
    private val pageStageHandler: PageStageHandler
) : BaseViewModel<BrowseUiEvent>() {

    private val listOfMovie = mutableListOf<MovieVO>()

    fun getPopularMovieList(page: Int = 1) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            repository.getPopularMovieList(page).suspendEither({
                emit(BrowseUiEvent.Error(it.message))
            }, {
                if (it.currentPage.isFirstPage()) {
                    listOfMovie.clear()
                }
                pageStageHandler.totalPage = it.totalPage
                pageStageHandler.currentPage = it.currentPage
                pageStageHandler.increaseCurrentPage()
                listOfMovie.addAll(it.list)
                emit(BrowseUiEvent.SuccessMovieList(listOfMovie))
            })
        }
    }


    fun onListEndReachPopularList() {
        if (pageStageHandler.isRangeInTotalPage()) {
            viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
                getPopularMovieList(page = pageStageHandler.currentPage,)
            }
        }
    }

    fun getSearchMovieList(page: Int = 1, query: String) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            repository.getSearchMovieList(page = page, query = query).suspendEither({
                emit(BrowseUiEvent.Error(it.message))
            }, {
                if (it.currentPage.isFirstPage()) {
                    listOfMovie.clear()
                }
                pageStageHandler.totalPage = it.totalPage
                pageStageHandler.currentPage = it.currentPage
                pageStageHandler.increaseCurrentPage()
                listOfMovie.addAll(it.list)
                emit(BrowseUiEvent.SuccessMovieList(listOfMovie))
            })
        }
    }


    fun onListEndReachSearchList(query: String) {
        if (pageStageHandler.isRangeInTotalPage()) {
            viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
                getSearchMovieList(page = pageStageHandler.currentPage, query = query)
            }
        }
    }

}
