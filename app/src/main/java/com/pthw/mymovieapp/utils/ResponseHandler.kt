package com.pthw.mymovieapp.utils

import com.pthw.mymovieapp.data.network.response.ErrorResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import retrofit2.Response

internal suspend fun <T, R> handle(
    apiCall: suspend () -> Response<DataResponse<T>>,
    errorHandler: suspend (DataResponse<T>) -> Either<ErrorResponse, R> = {
        Either.Left(
            ErrorResponse(
                message = it.errorMessage ?: "Something wrong",

                )
        )
    },
    handler: suspend (T?) -> Either<ErrorResponse, R>
): Either<ErrorResponse, R> {
    val call = apiCall()
    val body = call.body()

    return if (call.isSuccessful && body?.data != null) {
        handler(body.data)
    } else {
        Either.Left(
            ErrorResponse(
                message = body?.errorMessage ?: "Something wrong"
            )
        )
    }
}

internal suspend fun <T, R> handleForCustom(
    apiCall: suspend () -> Response<T>,
    errorHandler: suspend (T) -> Either<ErrorResponse, R> = {
        Either.Left(
            ErrorResponse(
                message = "Something wrong",

                )
        )
    },
    handler: suspend (T?) -> Either<ErrorResponse, R>
): Either<ErrorResponse, R> {
    val call = apiCall()
    val body = call.body()

    return if (call.isSuccessful && body != null) {
        handler(body)
    } else {
        Either.Left(
            ErrorResponse(
                message = "Something wrong"
            )
        )
    }
}

internal suspend fun <R> handleForEmpty(
    apiCall: suspend () -> Response<DataEmptyResponse>,
    errorHandler: suspend (DataEmptyResponse) -> Either<ErrorResponse, R> = {
        Either.Left(
            ErrorResponse(
                message = it.errorMessage ?: "Something wrong",
            )
        )
    },
    handler: suspend (DataEmptyResponse) -> Either<ErrorResponse, R>
): Either<ErrorResponse, R> {
    val call = apiCall()
    val body = call.body()

    return if (call.isSuccessful && body?.errorMessage == "Success") {
        handler(body)
    } else {
        Either.Left(
            ErrorResponse(
                message = body?.errorMessage ?: "Something wrong"
            )
        )
    }
}

internal suspend fun <T, R> handleForMeta(
    apiCall: suspend () -> Response<DataMetaResponse<T>>,
    errorHandler: suspend (DataMetaResponse<T>) -> Either<ErrorResponse, R> = {
        Either.Left(
            ErrorResponse(
                message = it.errorMessage ?: "Something wrong",

                )
        )
    },
    handler: suspend (DataMetaResponse<T>?) -> Either<ErrorResponse, R>
): Either<ErrorResponse, R> {
    val call = apiCall()
    val body = call.body()

    return if (call.isSuccessful && body?.data != null) {
        handler(body)
    } else {
        Either.Left(
            ErrorResponse(
                message = body?.errorMessage ?: "Something wrong"
            )
        )
    }
}


//-- Handle for zip
internal suspend fun <T, R> handleForZip(
    apiCall: suspend () -> Flow<Triple<Response<DataMetaResponse<T>>,Response<DataMetaResponse<T>>,Response<DataMetaResponse<T>>>>,
    errorHandler: suspend (DataResponse<T>) -> Either<ErrorResponse, R> = {
        Either.Left(
            ErrorResponse(
                message = it.errorMessage ?: "Something wrong",

                )
        )
    },
    handler: suspend (Triple<DataMetaResponse<T>,DataMetaResponse<T>,DataMetaResponse<T>>?) -> Either<ErrorResponse, R>
): Either<ErrorResponse, R> {
    val flow = apiCall().first()

    val firstCall = flow.first
    val firstBody = firstCall.body()
    val secondCall = flow.second
    val secondBody = secondCall.body()
    val thirdCall = flow.third
    val thirdBody = thirdCall.body()

    return if (firstCall.isSuccessful && firstBody?.data != null && secondCall.isSuccessful && secondBody?.data != null && thirdCall.isSuccessful && thirdBody?.data != null) {
        handler(Triple(firstBody,secondBody,thirdBody))
    } else {
        Either.Left(ErrorResponse(message = "Something wrong"))
    }
}