package com.grevi.aywapet.model

import com.google.gson.annotations.SerializedName

data class Pets(
        @SerializedName("_id") val id : String,
        @SerializedName("clinic") val clinicId : Clinic,
        @SerializedName("types") val types : Animal,
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