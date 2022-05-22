package com.pthw.mymovieapp.utils

import com.squareup.moshi.Json

data class DataResponse<T>(

    @field:Json(name = "results")
    val data: T?,

    @field:Json(name = "message")
    val errorMessage: String?
)

data class DataEmptyResponse(

    @field:Json(name = "message")
    val errorMessage: String?
)

data class DataMetaResponse<T>(
    @field:Json(name = "results")
    val data: List<T>?,

    @field:Json(name = "message")
    val errorMessage: String?,

    @field:Json(name = "page")
    val currentPage: Int,
    @field:Json(name = "total_pages")
    val totalPage: Int
)