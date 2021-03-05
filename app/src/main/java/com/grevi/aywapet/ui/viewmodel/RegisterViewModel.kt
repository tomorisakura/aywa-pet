package com.grevi.aywapet.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grevi.aywapet.datasource.response.KabupatenResponse
import com.grevi.aywapet.datasource.response.KecamatanResponse
import com.grevi.aywapet.datasource.response.ProvinceResponse
import com.grevi.aywapet.datasource.response.VerifyResponse
import com.grevi.aywapet.repository.RepositoryImpl
import com.grevi.aywapet.utils.ApiException
import com.grevi.aywapet.utils.RegisHelper
import com.grevi.aywapet.utils.SharedUtils
import com.grevi.aywapet.utils.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val repositoryImpl: RepositoryImpl, private val sharedUtils: SharedUtils) : ViewModel() {

    private val _province = MutableLiveData<State<ProvinceResponse>>()
    private val _district = MutableLiveData<State<KabupatenResponse>>()
    private val _subDistrict = MutableLiveData<State<KecamatanResponse>>()
    private val _emailVerify = MutableLiveData<State<VerifyResponse>>()
    private val _loginState = MutableLiveData<Boolean>()

    var regisHelper : RegisHelper? = null
    val province : MutableLiveData<State<ProvinceResponse>> get() = _province
    val loginState : MutableLiveData<Boolean> get() = _loginState

    private val TAG = RegisterViewModel::class.java.simpleName

    init {
        getProvince()
        getLoginState()
    }

    private fun getProvince() : LiveData<State<ProvinceResponse>> {
        viewModelScope.launch {
            val data = repositoryImpl.getProvince()
            try {
                data.let { _province.postValue(data) }
            }catch (e : ApiException) {
                _province.postValue(State.Error(e))
            }
        }
        return _province
    }

    fun getDistrict(id : String) : LiveData<State<KabupatenResponse>> {
        viewModelScope.launch {
            val data = repositoryImpl.getDistrict(id)
            try {
                _district.postValue(data)
            }catch (e : ApiException) {
                _district.postValue(State.Error(e))
            }
        }
        return _district
    }

    fun getSubDistrict(id : String) : LiveData<State<KecamatanResponse>> {
        viewModelScope.launch {
            val data = repositoryImpl.getSubDistrict(id)
            try {
                _subDistrict.postValue(data)
            }catch (e : ApiException) {
                _subDistrict.postValue(State.Error(e))
            }
        }
        return _subDistrict
    }

    fun createUser(name : String, phone : String, address : String, email : String, uid : String) {
        viewModelScope.launch {
            val remote = repositoryImpl.createUser(name, phone, address, email, uid)
            remote.let {
                when(it) {
                    is State.Error -> Log.e(TAG, it.toString())
                    is State.Success -> {
                        if (it.data.status) {
                            delay(2000L)
                            getEmail(email)
                            regisHelper?.success()
                        }
                    }
                    else -> Log.i(TAG, "")
                }
            }
        }
    }

    fun getEmail(email: String) : LiveData<State<VerifyResponse>> {
        viewModelScope.launch {
            val remote = repositoryImpl.getEmailVerify(email)
            _emailVerify.postValue(State.Loading())
            try {
                sharedUtils.setLoginKey()
                sharedUtils.setUserKey(email)
                _emailVerify.postValue(remote)
            }catch (e : ApiException) {
                e.printStackTrace()
                _emailVerify.postValue(State.Error(e))
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