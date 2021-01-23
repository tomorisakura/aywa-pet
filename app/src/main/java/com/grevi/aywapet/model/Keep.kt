package com.grevi.aywapet.model

import com.google.gson.annotations.SerializedName

data class Keep(
        @SerializedName("_id") val id : String,
        @SerializedName("pet_id") val petId : Pet,
        @SerializedName("users_id") val userId : UserKeep
)

data class Pet(
        @SerializedName("_id") val id : String,
        @SerializedName("clinic") val clinicId : String,
        @SerializedName("picture") val pictures : ArrayList<Pictures>,
        @SerializedName("nama_peliharaan") val petName : String,
        @SerializedName("uniqname") val uniqname : String,
        @SerializedName("pemilik_lama") val oldOwner : String,
        @SerializedName("jenis_kelamin") val gender : String,
        @SerializedName("berat_peliharaan") val weight : String,
        @SerializedName("ras_peliharaan") val ras : String,
        @SerializedName("umur_peliharaan") val age : String,
        @SerializedName("status_vaksin") val vaccine : String
)

data class UserKeep(
        @SerializedName("_id") val id : String,
        @SerializedName("name") val name : String,
        @SerializedName("no_hp") val phone : String,
        @SerializedName("email") val email : String,
        @SerializedName("alamat") val address : String,
)