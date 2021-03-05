package com.grevi.aywapet.repository

import android.util.Log
import com.grevi.aywapet.datasource.ApiResponse
import com.grevi.aywapet.datasource.response.*
import com.grevi.aywapet.datasource.services.ApiHelperImpl
import com.grevi.aywapet.db.DatabaseHelperImpl
import com.grevi.aywapet.db.entity.Users
import com.grevi.aywapet.repository.mapper.EntityMapperImpl
import com.grevi.aywapet.utils.SharedUtils
import com.grevi.aywapet.utils.State
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RepositoryImpl @Inject constructor(private val apiHelperImpl: ApiHelperImpl, private val databaseHelperImpl: DatabaseHelperImpl,
                                         private val mapperImpl : EntityMapperImpl, private val sharedUtils: SharedUtils) : Repository, ApiResponse() {

    private val TAG = RepositoryImpl::class.java.simpleName

    override suspend fun getAllPet() : State<PetResponse> {
        var token : String? = null
        databaseHelperImpl.getUser().collect { data -> data.map { token = it.token } }
        return apiResponse { apiHelperImpl.getPet(token!!) }
    }

    override suspend fun getAnimal() : State<AnimalResponse> {
        var token : String? = null
        databaseHelperImpl.getUser().collect { data -> data.map { token = it.token } }
        return apiResponse { apiHelperImpl.getAnimal(token!!) }
    }

    override suspend fun getPet(id : String) : State<PetDetailResponse> {
        var token : String? = null
        databaseHelperImpl.getUser().collect { data -> data.map { token = it.token } }
        return apiResponse { apiHelperImpl.getPetDetail(token!!, id) }
    }

    override suspend fun getEmailVerify(email : String) : State<VerifyResponse> {
        return apiResponse { apiHelperImpl.getEmailVerify(email).also {
            val data = it.body()?.result ?: return@also
            val token = it.body()?.token ?: return@also
            mapperImpl.mapToEntity(data, token = token).let { data ->
                sharedUtils.setUniqueKey(data.id)
                databaseHelperImpl.insertUser(data)
            }
        } }
    }

    override suspend fun createUser(name: String, phone: String, alamat: String, email: String, uid: String) : State<PostUserResponse> {
        return apiResponse { apiHelperImpl.createUser(name, phone, alamat, email, uid).also {
            val data = it.body()?.result ?: return@also
            val token = it.body()?.token ?: return@also
            mapperImpl.mapToEntity(data, token = token).also { data ->
                sharedUtils.setUniqueKey(data.id)
                databaseHelperImpl.insertUser(data)
            }
        } }
    }

    override suspend fun getKeepPet(idUsers : String) : State<GetKeepResponse> {
        var token : String? = null
        databaseHelperImpl.getUser().collect { data -> data.map { token = it.token } }
        return apiResponse { apiHelperImpl.getKeepPet(token!!, idUsers) }
    }

    override suspend fun keepPost(idPet : String, idUsers : String) : State<KeepPostResponse> {
        var token : String? = null
        databaseHelperImpl.getUser().collect { data -> data.map { token = it.token } }
        return apiResponse { apiHelperImpl.postKeep(token!!, idPet, idUsers) }
    }

    override suspend fun getKeepSuccess(idUsers: String) : State<GetKeepSuccessResponse> {
        var token : String? = null
        databaseHelperImpl.getUser().collect { data -> data.map { token = it.token } }
        return apiResponse { apiHelperImpl.getKeepSuccess(token!!, idUsers) }
    }

    override suspend fun getPetByType(idType : String) : State<PetResponse> {
        var token : String? = null
        databaseHelperImpl.getUser().collect { data -> data.map { token = it.token } }
        return apiResponse { apiHelperImpl.getPetByType(token!!, idType) }
    }

    override suspend fun getLocalUser(): Flow<MutableList<Users>> {
        return databaseHelperImpl.getUser()
    }

    override suspend fun getProvince(): State<ProvinceResponse> = apiResponse { apiHelperImpl.getProvince() }
    override suspend fun getDistrict(id: String): State<KabupatenResponse> = apiResponse { apiHelperImpl.getDistrict(id) }
    override suspend fun getSubDistrict(id: String): State<KecamatanResponse> = apiResponse { apiHelperImpl.getSubDistrict(id) }
    override suspend fun getSearchPet(ras: String): State<PetResponse> {
        var token : String? = null
        databaseHelperImpl.getUser().collect { data -> data.map { token = it.token } }
        return apiResponse { apiHelperImpl.getSearchPet(token!!, ras) }
    }

    override suspend fun createFlow(name: String, phone: String, alamat: String, email: String, uid: String): Flow<PostUserResponse> {
        return flow {
            apiHelperImpl.createUserFlow(name, phone, alamat, email, uid).collect { resp ->
                if (resp.isSuccessful) {
                    val data = resp.body() ?: return@collect
                    mapperImpl.mapToEntity(data.result, data.token).let {
                        databaseHelperImpl.insertUser(it)
                    }
                    emit(data)
                    Log.i(TAG, data.toString())
                }
            }
        }
    }

}