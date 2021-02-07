package com.grevi.aywapet.utils

sealed class State<out T> {
    object Data : State<Nothing>()
    data class Loading<T>(val msg : String = "Loading") : State<T>()
    data class Success<T>(val data : T) : State<T>()
    data class Error<T>(val exception : Throwable) : State<T>()
}