package com.grevi.aywapet.datasource.services

import com.grevi.aywapet.datasource.response.*
import retrofit2.Response

interface ApiHelper {
    suspend fun getPet() : Response<PetResponse>
    suspend fun getAnimal() : Response<AnimalResponse>
    suspend fun getPet(id : String) : Response<PetDetailResponse>
    suspend fun getEmailVerify(email : String) :Response<VerifyResponse>
    suspend fun createUser(name : String, phone : String, password : String, alamat : String, email : String, uid : String) : Response<PostUserResponse>
    suspend fun getKeepPet(idUser : String) : Response<GetKeepResponse>
    suspend fun postKeep(idPet : String, idUser: String) : Response<KeepPostResponse>
    suspend fun getKeepSuccess(idUser: String) : Response<GetKeepSuccessResponse>
    suspend fun getPetByType(idType : String) : Response<PetResponse>
}