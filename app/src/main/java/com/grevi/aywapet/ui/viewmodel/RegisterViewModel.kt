package com.grevi.aywapet.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grevi.aywapet.datasource.response.KabupatenResponse
import com.grevi.aywapet.datasource.response.KecamatanResponse
import com.grevi.aywapet.datasource.response.ProvinceResponse
import com.grevi.aywapet.datasource.response.VerifyResponse
import com.grevi.aywapet.repository.RepositoryImpl
import com.grevi.aywapet.utils.RegisHelper
import com.grevi.aywapet.utils.Resource
import com.grevi.aywapet.utils.SharedUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val repositoryImpl: RepositoryImpl, private val sharedUtils: SharedUtils) : ViewModel() {

    private val _province = MutableLiveData<Resource<ProvinceResponse>>()
    private val _district = MutableLiveData<Resource<KabupatenResponse>>()
    private val _subDistrict = MutableLiveData<Resource<KecamatanResponse>>()
    private val _emailVerify = MutableLiveData<Resource<VerifyResponse>>()
    private val _loginState = MutableLiveData<Boolean>()

    var regisHelper : RegisHelper? = null
    val province : MutableLiveData<Resource<ProvinceResponse>> get() = _province
    val loginState : MutableLiveData<Boolean> get() = _loginState

    init {
        getProvince()
        getLoginState()
    }

    private fun getProvince() : LiveData<Resource<ProvinceResponse>> {
        viewModelScope.launch {
            val data = repositoryImpl.getProvince()
            try {
                data.data?.let { _province.postValue(Resource.success(data = it)) }
            }catch (e : Exception) {
                _province.postValue(Resource.error(null, e.toString()))
            }
        }
        return _province
    }

    fun getDistrict(id : String) : LiveData<Resource<KabupatenResponse>> {
        viewModelScope.launch {
            val data = repositoryImpl.getDistrict(id)
            try {
                data.data?.let { _district.postValue(Resource.success(data = it)) }
            }catch (e : Exception) {
                _district.postValue(Resource.error(null, e.toString()))
            }
        }
        return _district
    }

    fun getSubDistrict(id : String) : LiveData<Resource<KecamatanResponse>> {
        viewModelScope.launch {
            val data = repositoryImpl.getSubDistrict(id)
            try {
                data.data?.let { _subDistrict.postValue(Resource.success(data = it)) }
            }catch (e : Exception) {
                _subDistrict.postValue(Resource.error(null, e.toString()))
            }
        }
        return _subDistrict
    }

    fun createUser(name : String, phone : String, address : String, email : String, uid : String) {
        viewModelScope.launch {
            val remote = repositoryImpl.createUser(name, phone, address, email, uid)
            remote.data?.let {
                if (it.status) {
                    delay(2000L)
                    getEmail(email)
                    regisHelper?.success()
                }
            }
        }
    }

    fun getEmail(email: String) : LiveData<Resource<VerifyResponse>> {
        viewModelScope.launch {
            val remote = repositoryImpl.getEmailVerify(email)
            _emailVerify.postValue(Resource.loading())
            try {
                sharedUtils.setLoginKey()
                sharedUtils.setUserKey(email)
                remote.data?.let { _emailVerify.postValue(Resource.success(data = it)) }
            }catch (e : Exception) {
                e.printStackTrace()
                _emailVerify.postValue(Resource.error(null, e.toString()))
            }
        }
        return _emailVerify
    }

    private fun getLoginState() : LiveData<Boolean> {
        viewModelScope.launch {
            _loginState.postValue(sharedUtils.getLoginShared())
        }
        return  _loginState
    }

}