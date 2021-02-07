package com.grevi.aywapet.datasource.response

import com.google.gson.annotations.SerializedName
import com.grevi.aywapet.model.User

data class PostUserResponse(
        @SerializedName("method") val method : String,
        @SerializedName("status") val status : Boolean,
        @SerializedName("code") val code : Int,
        @SerializedName("token") val token : String,
        @SerializedName("result") val result : User
)
