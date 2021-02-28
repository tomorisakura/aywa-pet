package com.grevi.aywapet.datasource.services

import com.grevi.aywapet.datasource.response.*
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface ApiHelper {
    suspend fun getPet(token : String) : Response<PetResponse>
    suspend fun getAnimal(token : String) : Response<AnimalResponse>
    suspend fun getPetDetail(token : String, id : String) : Response<PetDetailResponse>
    suspend fun getEmailVerify(email : String) :Response<VerifyResponse>
    suspend fun createUser(name : String, phone : String, alamat : String, email : String, uid : String) : Response<PostUserResponse>
    suspend fun getKeepPet(token : String, idUser : String) : Response<GetKeepResponse>
    suspend fun postKeep(token : String, idPet : String, idUser: String) : Response<KeepPostResponse>
    suspend fun getKeepSuccess(token : String, idUser: String) : Response<GetKeepSuccessResponse>
    suspend fun getPetByType(token : String, idType : String) : Response<PetResponse>
    suspend fun getProvince() : Response<ProvinceResponse>
    suspend fun getDistrict(id : String) : Response<KabupatenResponse>
    suspend fun getSubDistrict(id : String) : Response<KecamatanResponse>
    suspend fun getSearchPet(token: String, ras : String) : Response<PetResponse>
    suspend fun createUserFlow(name : String, phone : String, alamat : String, email : String, uid : String) : Flow<Response<PostUserResponse>>
}