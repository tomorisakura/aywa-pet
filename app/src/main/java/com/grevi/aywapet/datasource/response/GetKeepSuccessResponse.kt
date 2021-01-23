package com.grevi.aywapet.datasource.response

import com.google.gson.annotations.SerializedName
import com.grevi.aywapet.model.KeepSuccess

data class GetKeepSuccessResponse(
        @SerializedName("method") val method : String,
        @SerializedName("status") val status : Boolean,
        @SerializedName("code") val code : Int,
        @SerializedName("result") val result : ArrayList<KeepSuccess>
)
