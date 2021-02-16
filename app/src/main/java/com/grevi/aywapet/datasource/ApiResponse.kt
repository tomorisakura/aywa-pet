package com.grevi.aywapet.datasource

import com.grevi.aywapet.utils.Resource
import retrofit2.Response
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

open class ApiResponse {
    suspend fun <T : Any>apiResponse(call : suspend () -> Response<T>) : Resource<T> {
        return try {
            call.invoke().apply {
                if (this.isSuccessful) {
                    val body = this.body()
                    if (body != null) return Resource.success(body)
                }
            }
            Resource.exception(msg = "${call.invoke().code()} : ${call.invoke().errorBody()}")
        } catch (e : Exception) {
            return when(e) {
                is UnknownHostException -> {
                    e.printStackTrace()
                    Resource.exception(msg = "host err : $e")
                }
                is ConnectException -> {
                    e.printStackTrace()
                    Resource.exception(msg = "connection err : $e")
                }
                is SocketTimeoutException -> {
                    e.printStackTrace()
                    Resource.exception(msg = "connection err : $e")
                }
                else -> {
                    e.printStackTrace()
                    Resource.exception(msg = e.toString())
                }
            }
        }
    }
}