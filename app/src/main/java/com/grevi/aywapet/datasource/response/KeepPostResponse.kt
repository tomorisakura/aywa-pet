package com.grevi.aywapet.datasource.response

import com.google.gson.annotations.SerializedName

data class KeepPostResponse(
    @SerializedName("method") val method : String,
    @SerializedName("status") val status : Boolean,
    @SerializedName("code") val code : Int
)