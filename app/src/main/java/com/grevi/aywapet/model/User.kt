package com.grevi.aywapet.model

import com.google.gson.annotations.SerializedName

data class User(
        @SerializedName("_id") val id : String,
        @SerializedName("name") val name : String,
        @SerializedName("username") val username : String,
        @SerializedName("no_hp") val phone : String,
        @SerializedName("email") val email : String,
        @SerializedName("alamat") val address : String,
        @SerializedName("uid_auth") val uid : String
)
