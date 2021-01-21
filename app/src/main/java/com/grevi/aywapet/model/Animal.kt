package com.grevi.aywapet.model

import com.google.gson.annotations.SerializedName

data class Animal(
        @SerializedName("_id") val id : String,
        @SerializedName("jenis") val jenis : String
)
