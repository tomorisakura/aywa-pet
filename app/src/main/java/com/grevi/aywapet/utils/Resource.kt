package com.grevi.aywapet.utils

class Resource<out T>(val status : STATUS, val data : T?, val msg : String?) {

    enum class STATUS {
        SUCCESS,
        LOADING,
        ERROR,
        EXCEPTION
    }

    companion object {
        fun <T>success(data : T, msg : String? = null) : Resource<T> = Resource(STATUS.SUCCESS, data, msg)
        fun <T>loading(data : T? = null, msg : String = "Loading") : Resource<T> = Resource(STATUS.LOADING, data, msg)
        fun <T>error(data : T? = null, msg: String) : Resource<T> = Resource(STATUS.ERROR, data, msg)
        fun <T>exception(data : T? = null, msg: String) : Resource<T> = Resource(STATUS.EXCEPTION, data, msg)
    }

}