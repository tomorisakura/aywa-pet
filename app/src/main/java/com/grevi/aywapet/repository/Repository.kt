package com.grevi.aywapet.repository

import com.grevi.aywapet.datasource.response.*
import com.grevi.aywapet.db.entity.Users
import com.grevi.aywapet.utils.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun getAllPet() : Resource<PetResponse>
    suspend fun getAnimal() : Resource<AnimalResponse>
    suspend fun getPet(id : String) : Resource<PetDetailResponse>
    suspend fun getEmailVerify(email : String) : Resource<VerifyResponse>
    suspend fun createUser(name: String, phone: String, password: String, alamat: String, email: String, uid: String) : Resource<PostUserResponse>
    suspend fun getKeepPet(idUsers : String) : Resource<GetKeepResponse>
    suspend fun keepPost(idPet : String, idUsers : String) : Resource<KeepPostResponse>
    suspend fun getKeepSuccess(idUsers: String) : Resource<GetKeepSuccessResponse>
    suspend fun getPetByType(idType : String) : Resource<PetResponse>
    suspend fun getLocalUser() : Flow<MutableList<Users>>
}