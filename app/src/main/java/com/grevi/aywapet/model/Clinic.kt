package com.grevi.aywapet.model

import com.google.gson.annotations.SerializedName

data class Clinic(
        @SerializedName("_id") val id : String,
        @SerializedName("clinic_name") val name : String,
        @SerializedName("email") val email : String,
        @SerializedName("no_hp") val phone : String,
        @SerializedName("alamat") val address : String
)