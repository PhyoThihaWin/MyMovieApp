package com.pthw.mymovieapp.utils

import javax.inject.Inject

class PageStageHandler @Inject constructor() {
    var totalPage = 0
    var currentPage = 0

    fun resetCurrentPage() {
        currentPage = 0
    }

    fun increaseCurrentPage() {
        currentPage++
    }

    fun isRangeInTotalPage(): Boolean = currentPage <= totalPage
}
