package com.pthw.mymovieapp.vos

data class MetaVO<T>(
    val list: List<T>,
    val currentPage: Int,
    val totalPage: Int
)
