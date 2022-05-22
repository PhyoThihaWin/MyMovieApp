package com.pthw.mymovieapp.mvvm.viewmodel

import androidx.lifecycle.viewModelScope
import com.pthw.mymovieapp.base.BaseViewModel
import com.pthw.mymovieapp.data.repository.MovieRepository
import com.pthw.mymovieapp.mvvm.event.HomeUiEvent
import com.pthw.mymovieapp.utils.PageStageHandler
import com.pthw.mymovieapp.utils.isFirstPage
import com.pthw.mymovieapp.vos.MovieVO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: MovieRepository,
    val pageStageHandler: PageStageHandler
) : BaseViewModel<HomeUiEvent>() {

    fun getHomeMovie() {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            val homeMovieVO = repository.getHomeMovie()
            if (homeMovieVO == null) {
                emit(HomeUiEvent.Error("Something wrong."))
            } else {
                emit(HomeUiEvent.SuccessHomeMovie(homeMovieVO))
            }
        }
    }


    fun fetchHomeMovie(page: Int = 1) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            repository.fetchHomeMovie(page).suspendEither({
                emit(HomeUiEvent.Error(it.message))
            }, {
                if (it == null) {
                    emit(HomeUiEvent.Error("Something wrong."))
                } else {
                    emit(HomeUiEvent.SuccessHomeMovie(it))
                }
            })
        }
    }


    private val listOfMovie = mutableListOf<MovieVO>()

    fun getPopularMovieList(page: Int = 1) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            repository.getPopularMovieList(page).suspendEither({
                emit(HomeUiEvent.Error(it.message))
            }, {
                if (it.currentPage.isFirstPage()) {
                    listOfMovie.clear()
                }
                pageStageHandler.totalPage = it.totalPage
                pageStageHandler.currentPage = it.currentPage
                pageStageHandler.increaseCurrentPage()
                listOfMovie.addAll(it.list)
                emit(HomeUiEvent.SuccessMovieList(listOfMovie))
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

    fun getTopRatedMovieList(page: Int = 1) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            repository.getTopRatedMovieList(page).suspendEither({
                emit(HomeUiEvent.Error(it.message))
            }, {
                if (it.currentPage.isFirstPage()) {
                    listOfMovie.clear()
                }
                pageStageHandler.totalPage = it.totalPage
                pageStageHandler.currentPage = it.currentPage
                pageStageHandler.increaseCurrentPage()
                listOfMovie.addAll(it.list)
                emit(HomeUiEvent.SuccessMovieList(listOfMovie))
            })
        }
    }


    fun onListEndReachTopRatedList() {
        if (pageStageHandler.isRangeInTotalPage()) {
            viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
                getTopRatedMovieList(page = pageStageHandler.currentPage,)
            }
        }
    }

    fun getUpComingMovieList(page: Int = 1) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            repository.getUpComingMovieList(page).suspendEither({
                emit(HomeUiEvent.Error(it.message))
            }, {
                if (it.currentPage.isFirstPage()) {
                    listOfMovie.clear()
                }
                pageStageHandler.totalPage = it.totalPage
                pageStageHandler.currentPage = it.currentPage
                pageStageHandler.increaseCurrentPage()
                listOfMovie.addAll(it.list)
                emit(HomeUiEvent.SuccessMovieList(listOfMovie))
            })
        }
    }


    fun onListEndReachUpComingList() {
        if (pageStageHandler.isRangeInTotalPage()) {
            viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
                getUpComingMovieList(page = pageStageHandler.currentPage,)
            }
        }
    }

}
