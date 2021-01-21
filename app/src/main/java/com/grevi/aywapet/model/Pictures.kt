package com.grevi.aywapet.model

import com.google.gson.annotations.SerializedName

data class Pictures(
        @SerializedName("id") val id : String,
        @SerializedName("pic_name") val picName : String,
        @SerializedName("pic_compress") val picUrl : String
)
