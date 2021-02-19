package com.grevi.aywapet.ui.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grevi.aywapet.datasource.response.VerifyResponse
import com.grevi.aywapet.repository.RemoteRepos
import com.grevi.aywapet.utils.RegisHelper
import com.grevi.aywapet.utils.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RegisViewModel @ViewModelInject constructor(private val remoteRepos: RemoteRepos) : ViewModel() {
    var name = MutableLiveData<String>()

    var phone = MutableLiveData<String>()
    var address = MutableLiveData<String>()
    var password = MutableLiveData<String>()
    var email = MutableLiveData<String>()
    var nik = MutableLiveData<String>()

    var uid = MutableLiveData<String>()
    var regisHelper : RegisHelper? = null

    private val _emailData = MutableLiveData<Resource<VerifyResponse>>()

    fun submit() {
        viewModelScope.launch {
            val name = name.value
            val phone = phone.value
            val address = address.value
            val password = password.value
            val email = email.value
            val uid = uid.value
            val nik = nik.value

            when {
                name.isNullOrEmpty() -> regisHelper?.message("Nama ga boleh kosong")
                phone.isNullOrEmpty() -> regisHelper?.message("No. Hp ga boleh kosong")
                address.isNullOrEmpty() -> regisHelper?.message("Alamat juga ga boleh kosong")
                password.isNullOrEmpty() -> regisHelper?.message("Password ga boleh kosong")
                nik.isNullOrEmpty() -> regisHelper?.message("NIK ga boleh kosong")
                else -> {
                    when {
                        password.length < 8 -> regisHelper?.message("Password terlalu pendek")
                        phone.length < 11 -> regisHelper?.message("Nomor Hp tidak valid")
                        nik.length < 15 -> regisHelper?.message("NIK tidak valid")
                        else -> {
                            val data = remoteRepos.createUser(name, phone, password, address, nik, email!!, uid!!)
                            data.data?.let {
                                if (it.status) {
                                    delay(2000L)
                                    regisHelper?.success(email)
                                } else {
                                    regisHelper?.message("terjadi kesalahan, mohon tunggu beberapa saat")
                                }
                            }
                        }
                    }
                }
            }

        }
    }

    internal fun getEmailVerify(email : String) : LiveData<Resource<VerifyResponse>> {
        viewModelScope.launch {
            val data = remoteRepos.getEmailVerify(email)
            _emailData.postValue(Resource.loading(msg = "Load"))
            try {
                _emailData.postValue(Resource.success(data = data.data!!))
            } catch (e : Exception) {
                _emailData.postValue(Resource.error(msg = e.toString()))
            }
        }
        return _emailData
    }
}