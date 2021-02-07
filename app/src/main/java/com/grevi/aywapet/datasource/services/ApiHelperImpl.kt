package com.grevi.aywapet.datasource.services

import com.grevi.aywapet.datasource.response.*
import retrofit2.Response
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(private val apiService: ApiService) : ApiHelper {
    override suspend fun getPet(token : String): Response<PetResponse> {
        return apiService.getPet(token)
    }

    override suspend fun getPetDetail(token : String, id: String): Response<PetDetailResponse> {
        return apiService.getPetId(token, id)
    }

    override suspend fun getAnimal(token : String): Response<AnimalResponse> {
        return apiService.getAnimal(token)
    }

    override suspend fun getEmailVerify(email: String): Response<VerifyResponse> {
        return apiService.getEmailVerify(email)
    }

    override suspend fun createUser(name: String, phone: String, password: String, alamat: String, email: String, uid: String): Response<PostUserResponse> {
        return apiService.createUser(name, phone, password, alamat, email, uid)
    }

    override suspend fun getKeepPet(token : String, idUser: String): Response<GetKeepResponse> {
        return apiService.getKeepPet(token, idUser)
    }

    override suspend fun postKeep(token : String, idPet: String, idUser: String): Response<KeepPostResponse> {
        return apiService.postKeep(token, idPet, idUser)
    }

    override suspend fun getKeepSuccess(token : String, idUser: String): Response<GetKeepSuccessResponse> {
        return apiService.getSuccessKeep(token, idUser)
    }

    override suspend fun getPetByType(token : String, idType: String): Response<PetResponse> {
        return apiService.getPetByType(token, idType)
    }

}