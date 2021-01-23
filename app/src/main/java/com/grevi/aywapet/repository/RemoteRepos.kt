package com.grevi.aywapet.repository

import com.grevi.aywapet.datasource.ApiResponse
import com.grevi.aywapet.datasource.response.*
import com.grevi.aywapet.datasource.services.ApiHelperImpl
import com.grevi.aywapet.utils.Resource
import javax.inject.Inject

class RemoteRepos @Inject constructor(private val apiHelperImpl: ApiHelperImpl) : ApiResponse() {

    suspend fun getAllPet() : Resource<PetResponse> {
        return apiResponse { apiHelperImpl.getPet() }
    }

    suspend fun getAnimal() : Resource<AnimalResponse> {
        return apiResponse { apiHelperImpl.getAnimal() }
    }

    suspend fun getPet(id : String) : Resource<PetDetailResponse> {
        return apiResponse { apiHelperImpl.getPet(id) }
    }

    suspend fun getEmailVerify(email : String) : Resource<VerifyResponse> {
        return apiResponse { apiHelperImpl.getEmailVerify(email) }
    }

    suspend fun createUser(name: String, phone: String, password: String, alamat: String, email: String, uid: String) : Resource<PostUserResponse> {
        return apiResponse { apiHelperImpl.createUser(name, phone, password, alamat, email, uid) }
    }

    suspend fun getKeepPet(idUser : String) : Resource<GetKeepResponse> {
        return apiResponse { apiHelperImpl.getKeepPet(idUser) }
    }

    suspend fun keepPost(idPet : String, idUser: String) : Resource<KeepPostResponse> {
        return apiResponse { apiHelperImpl.postKeep(idPet, idUser) }
    }

    suspend fun getKeepSuccess(idUser: String) : Resource<GetKeepSuccessResponse> {
        return apiResponse { apiHelperImpl.getKeepSuccess(idUser) }
    }

    suspend fun getPetByType(idType : String) : Resource<PetResponse> {
        return apiResponse { apiHelperImpl.getPetByType(idType) }
    }

}