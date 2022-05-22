package com.pthw.mymovieapp.data.network.response

import com.squareup.moshi.Json

data class ErrorResponse(
    @field:Json(name = "message")
    val message : String
)
