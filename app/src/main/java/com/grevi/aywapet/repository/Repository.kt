package com.grevi.aywapet.repository

import com.grevi.aywapet.datasource.response.*
import com.grevi.aywapet.db.entity.Users
import com.grevi.aywapet.utils.State
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun getAllPet() : State<PetResponse>
    suspend fun getAnimal() : State<AnimalResponse>
    suspend fun getPet(id : String) : State<PetDetailResponse>
    suspend fun getEmailVerify(email : String) : State<VerifyResponse>
    suspend fun createUser(name: String, phone: String, alamat: String, email: String, uid: String) : State<PostUserResponse>
    suspend fun getKeepPet(idUsers : String) : State<GetKeepResponse>
    suspend fun keepPost(idPet : String, idUsers : String) : State<KeepPostResponse>
    suspend fun getKeepSuccess(idUsers: String) : State<GetKeepSuccessResponse>
    suspend fun getPetByType(idType : String) : State<PetResponse>
    suspend fun getLocalUser() : Flow<MutableList<Users>>
    suspend fun getProvince() : State<ProvinceResponse>
    suspend fun getDistrict(id : String) : State<KabupatenResponse>
    suspend fun getSubDistrict(id : String) : State<KecamatanResponse>
    suspend fun getSearchPet(ras : String) : State<PetResponse>
    suspend fun createFlow(name: String, phone: String, alamat: String, email: String, uid: String) : Flow<PostUserResponse>
}