package com.pthw.mymovieapp.utils

interface UniMapper<S, T> {
    suspend fun map(data: S): T
}