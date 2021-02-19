package com.grevi.aywapet.repository

import com.grevi.aywapet.datasource.ApiResponse
import com.grevi.aywapet.datasource.response.*
import com.grevi.aywapet.datasource.services.ApiHelperImpl
import com.grevi.aywapet.db.DatabaseHelperImpl
import com.grevi.aywapet.db.entity.Users
import com.grevi.aywapet.repository.mapper.EntityMapperImpl
import com.grevi.aywapet.utils.Resource
import com.grevi.aywapet.utils.SharedUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class RemoteRepos @Inject constructor(private val apiHelperImpl: ApiHelperImpl,private val databaseHelperImpl: DatabaseHelperImpl,
                                      private val mapperImpl : EntityMapperImpl, private val sharedUtils: SharedUtils) : Repository, ApiResponse() {

    override suspend fun getAllPet() : Resource<PetResponse> {
        var token : String? = null
        databaseHelperImpl.getUser().collect { data -> data.map { token = it.token } }
        return apiResponse { apiHelperImpl.getPet(token!!) }
    }

    override suspend fun getAnimal() : Resource<AnimalResponse> {
        var token : String? = null
        databaseHelperImpl.getUser().collect { data -> data.map { token = it.token } }
        return apiResponse { apiHelperImpl.getAnimal(token!!) }
    }

    override suspend fun getPet(id : String) : Resource<PetDetailResponse> {
        var token : String? = null
        databaseHelperImpl.getUser().collect { data -> data.map { token = it.token } }
        return apiResponse { apiHelperImpl.getPetDetail(token!!, id) }
    }

    override suspend fun getEmailVerify(email : String) : Resource<VerifyResponse> {
        return apiResponse { apiHelperImpl.getEmailVerify(email).also {
            val data = it.body()?.result ?: return@also
            val token = it.body()?.token ?: return@also
            mapperImpl.mapToEntity(data, token = token).let { data ->
                sharedUtils.setUniqueKey(data.id)
                databaseHelperImpl.insertUser(data)
            }
        } }
    }

    override suspend fun createUser(name: String, phone: String, password: String, alamat: String, nik : String, email: String, uid: String) : Resource<PostUserResponse> {
        return apiResponse { apiHelperImpl.createUser(name, phone, password, alamat, nik, email, uid).also {
            val data = it.body()?.result ?: return@also
            val token = it.body()?.token ?: return@also
            mapperImpl.mapToEntity(data, token = token).let { data ->
                sharedUtils.setUniqueKey(data.id)
                databaseHelperImpl.insertUser(data)
            }
        } }
    }

    override suspend fun getKeepPet(idUsers : String) : Resource<GetKeepResponse> {
        var token : String? = null
        databaseHelperImpl.getUser().collect { data -> data.map { token = it.token } }
        return apiResponse { apiHelperImpl.getKeepPet(token!!, idUsers) }
    }

    override suspend fun keepPost(idPet : String, idUsers : String) : Resource<KeepPostResponse> {
        var token : String? = null
        databaseHelperImpl.getUser().collect { data -> data.map { token = it.token } }
        return apiResponse { apiHelperImpl.postKeep(token!!, idPet, idUsers) }
    }

    override suspend fun getKeepSuccess(idUsers: String) : Resource<GetKeepSuccessResponse> {
        var token : String? = null
        databaseHelperImpl.getUser().collect { data -> data.map { token = it.token } }
        return apiResponse { apiHelperImpl.getKeepSuccess(token!!, idUsers) }
    }

    override suspend fun getPetByType(idType : String) : Resource<PetResponse> {
        var token : String? = null
        databaseHelperImpl.getUser().collect { data -> data.map { token = it.token } }
        return apiResponse { apiHelperImpl.getPetByType(token!!, idType) }
    }

    override suspend fun getLocalUser(): Flow<MutableList<Users>> {
        return databaseHelperImpl.getUser()
    }

}