package com.grevi.aywapet.datasource

import com.grevi.aywapet.utils.ApiException
import com.grevi.aywapet.utils.State
import retrofit2.Response

open class ApiResponse {
    suspend fun <T : Any>apiResponse(call : suspend () -> Response<T>) : State<T> {
        call.invoke().also {
            try {
                if (it.isSuccessful) {
                    val body = it.body() ?: return@also
                    return State.Success(body)
                }
            }catch (e : Exception) {
                throw ApiException(e.toString())
            }
        }
        return State.Data
    }
}