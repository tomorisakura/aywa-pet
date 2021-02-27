package com.grevi.aywapet.datasource.response

import com.google.gson.annotations.SerializedName

data class KecamatanResponse(
        @SerializedName("method") val method : String,
        @SerializedName("status") val status : Boolean,
        @SerializedName("code") val code : Int,
        @SerializedName("result") val result : ArrayList<Kecamatan>
)

data class Kecamatan(
        @SerializedName("id") val id : String,
        @SerializedName("nama") val name : String
)