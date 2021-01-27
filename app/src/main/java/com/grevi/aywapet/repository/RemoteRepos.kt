package com.grevi.aywapet.repository

import com.grevi.aywapet.datasource.ApiResponse
import com.grevi.aywapet.datasource.response.*
import com.grevi.aywapet.datasource.services.ApiHelperImpl
import com.grevi.aywapet.db.DatabaseHelperImpl
import com.grevi.aywapet.repository.mapper.EntityMapperImpl
import com.grevi.aywapet.utils.Resource
import com.grevi.aywapet.utils.SharedUtils
import kotlinx.coroutines.delay
import javax.inject.Inject

class RemoteRepos @Inject constructor(private val apiHelperImpl: ApiHelperImpl,private val databaseHelperImpl: DatabaseHelperImpl,
                                      private val mapperImpl : EntityMapperImpl, private val sharedUtils: SharedUtils) : Repository, ApiResponse() {

    override suspend fun getAllPet() : Resource<PetResponse> {
        return apiResponse { apiHelperImpl.getPet() }
    }

    override suspend fun getAnimal() : Resource<AnimalResponse> {
        return apiResponse { apiHelperImpl.getAnimal() }
    }

    override suspend fun getPet(id : String) : Resource<PetDetailResponse> {
        return apiResponse { apiHelperImpl.getPet(id) }
    }

    override suspend fun getEmailVerify(email : String) : Resource<VerifyResponse> {
        return apiResponse { apiHelperImpl.getEmailVerify(email).also {
            val data = it.body()?.result ?: return@also
            mapperImpl.mapToEntity(data, "jwt-token").let { data ->
                sharedUtils.setUniqueKey(data.id)
                databaseHelperImpl.insertUser(data)
            }
        } }
    }

    override suspend fun createUser(name: String, phone: String, password: String, alamat: String, email: String, uid: String) : Resource<PostUserResponse> {
        return apiResponse { apiHelperImpl.createUser(name, phone, password, alamat, email, uid).also {
            val data = it.body()?.result ?: return@also
            mapperImpl.mapToEntity(data, "jwt-token").let { data ->
                sharedUtils.setUniqueKey(data.id)
                databaseHelperImpl.insertUser(data)
            }
        } }
    }

    override suspend fun getKeepPet() : Resource<GetKeepResponse> {
        delay(1000L)
        val id = sharedUtils.getUniqueKey() ?: "null"
        return apiResponse { apiHelperImpl.getKeepPet(id) }
    }

    override suspend fun keepPost(idPet : String) : Resource<KeepPostResponse> {
        val id = sharedUtils.getUniqueKey() ?: "null"
        return apiResponse { apiHelperImpl.postKeep(idPet, id) }
    }

    override suspend fun getKeepSuccess() : Resource<GetKeepSuccessResponse> {
        delay(1000L)
        val id = sharedUtils.getUniqueKey() ?: "null"
        return apiResponse { apiHelperImpl.getKeepSuccess(id) }
    }

    override suspend fun getPetByType(idType : String) : Resource<PetResponse> {
        return apiResponse { apiHelperImpl.getPetByType(idType) }
    }

}