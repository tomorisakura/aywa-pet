package com.grevi.aywapet.datasource.services

import com.grevi.aywapet.datasource.response.*
import retrofit2.Response
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(private val apiService: ApiService) : ApiHelper {
    override suspend fun getPet(): Response<PetResponse> {
        return apiService.getPet()
    }

    override suspend fun getPet(id: String): Response<PetDetailResponse> {
        return apiService.getPetId(id)
    }

    override suspend fun getAnimal(): Response<AnimalResponse> {
        return apiService.getAnimal()
    }

    override suspend fun getEmailVerify(email: String): Response<VerifyResponse> {
        return apiService.getEmailVerify(email)
    }

    override suspend fun createUser(name: String, phone: String, password: String, alamat: String, email: String, uid: String): Response<PostUserResponse> {
        return apiService.createUser(name, phone, password, alamat, email, uid)
    }

    override suspend fun getKeepPet(idUser: String): Response<GetKeepResponse> {
        return apiService.getKeepPet(idUser)
    }

    override suspend fun postKeep(idPet: String, idUser: String): Response<KeepPostResponse> {
        return apiService.postKeep(idPet, idUser)
    }

}