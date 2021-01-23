package com.grevi.aywapet.ui.viewmodel

import android.os.CountDownTimer
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grevi.aywapet.datasource.response.*
import com.grevi.aywapet.db.entity.Users
import com.grevi.aywapet.repository.LocalRepos
import com.grevi.aywapet.repository.RemoteRepos
import com.grevi.aywapet.utils.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import java.lang.Exception

class MainViewModel @ViewModelInject constructor(private val remoteRepos: RemoteRepos, private val localRepos: LocalRepos) : ViewModel() {
    private val _petData = MutableLiveData<Resource<PetResponse>>()
    private val _animalData = MutableLiveData<Resource<AnimalResponse>>()
    private val _petDetailData = MutableLiveData<Resource<PetDetailResponse>>()
    private val _emailData = MutableLiveData<Resource<VerifyResponse>>()
    private val _userTableData = MutableLiveData<MutableList<Users>>()

    private val _keepDataResp = MutableLiveData<Resource<GetKeepResponse>>()
    private val _keepPostData = MutableLiveData<Resource<KeepPostResponse>>()
    private val _keepSuccessData = MutableLiveData<Resource<GetKeepSuccessResponse>>()

    private lateinit var countDownTimer: CountDownTimer
    private val initialCountDown : Long = 86400000 //24hours
    private val countDownInterval : Long = 1000
    private val _timeCount : MutableLiveData<String> = MutableLiveData()

    val petData : MutableLiveData<Resource<PetResponse>>
    get() = _petData

    val animalData : MutableLiveData<Resource<AnimalResponse>>
    get() = _animalData

    val timeCount : MutableLiveData<String>
    get() = _timeCount

    val userTable : MutableLiveData<MutableList<Users>>
    get() = _userTableData

    val keepData : MutableLiveData<Resource<GetKeepResponse>>
    get() = _keepDataResp

    val keepSuccessData : MutableLiveData<Resource<GetKeepSuccessResponse>> = _keepSuccessData

    init {
        getAllPet()
        getAnimal()
        getUserTable()
        getKeepPet()
        getKeepSuccessKeep()
    }

    internal fun getPetByType(idType : String) : LiveData<Resource<PetResponse>> {
        viewModelScope.launch {
            val data = remoteRepos.getPetByType(idType)
            _petData.postValue(Resource.loading(msg = "Memuat data..."))
            try {
                data.data?.let { _petData.postValue(Resource.success(data = it)) }
            } catch (e : Exception) {
                e.printStackTrace()
                _petData.postValue(Resource.error(msg = e.toString()))
            }
        }
        return _petData
    }

    private fun getKeepSuccessKeep() : LiveData<Resource<GetKeepSuccessResponse>> {
        viewModelScope.launch {
            val id = localRepos.getUser()[0].id
            val data = remoteRepos.getKeepSuccess(id)

            _keepSuccessData.postValue(Resource.loading(msg = "Load"))
            try {
                _keepSuccessData.postValue(Resource.success(data = data.data!!))
            } catch (e : Exception) {
                _keepSuccessData.postValue(Resource.error(msg = e.toString()))
            }
        }
        return _keepSuccessData
    }

    internal fun keepPostData(idPet : String) : LiveData<Resource<KeepPostResponse>> {
        viewModelScope.launch {
            val users = localRepos.getUser()
            val data = remoteRepos.keepPost(idPet, users[0].id)
            _keepPostData.postValue(Resource.loading(msg = "Load"))
            try {
                _keepPostData.postValue(Resource.success(data = data.data!!))
            } catch (e : Exception) {
                _keepPostData.postValue(Resource.error(msg = e.toString()))
            }
        }
        return _keepPostData
    }

    internal fun insertUser(id : String, username : String, email : String, name : String, uid : String, token : String) {
        viewModelScope.launch {
            delay(1000L)
            val users = Users(id, username, email, name, uid, token)
            localRepos.insertUser(users)
        }
    }

    private fun getUserTable() : LiveData<MutableList<Users>> {
        viewModelScope.launch {
            val data = localRepos.getUser()
            try {
                _userTableData.postValue(data)
            } catch (e : Exception) {
                _userTableData.postValue(mutableListOf())
            }
        }
        return _userTableData
    }

    private fun getKeepPet() : LiveData<Resource<GetKeepResponse>> {
        viewModelScope.launch {
            val id = localRepos.getUser()
            val data = remoteRepos.getKeepPet(id[0].id)
            _keepDataResp.postValue(Resource.loading(msg = "Load"))
            try {
                _keepDataResp.postValue(Resource.success(data = data.data!!))
            } catch (e : Exception) {
                _keepDataResp.postValue(Resource.error(msg = e.toString()))
            }
        }
        return _keepDataResp
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

    private fun getAllPet() : LiveData<Resource<PetResponse>> {
        viewModelScope.launch {
            val data = remoteRepos.getAllPet()
            _petData.postValue(Resource.loading(msg = "Memuat data..."))
            try {
                data.data?.let { _petData.postValue(Resource.success(data = it)) }
            } catch (e : Exception) {
                e.printStackTrace()
                _petData.postValue(Resource.error(msg = e.toString()))
            }
        }
        return _petData
    }

    private fun getAnimal() : LiveData<Resource<AnimalResponse>> {
        viewModelScope.launch {
            val data = remoteRepos.getAnimal()
            _animalData.postValue(Resource.loading(msg = "Memuat Kategori"))
            try {
                data.data?.let { _animalData.postValue(Resource.success(data = it)) }
            }catch (e : Exception) {
                e.printStackTrace()
                _animalData.postValue(Resource.error(msg = e.toString()))
            }
        }
        return _animalData
    }

    internal fun getPet(id : String) : LiveData<Resource<PetDetailResponse>> {
        viewModelScope.launch {
            val data = remoteRepos.getPet(id)
            _petDetailData.postValue(Resource.loading(msg = "Memuat data..."))
            try {
                data.data?.let { _petDetailData.postValue(Resource.success(data = it)) }
            } catch (e : Exception) {
                _petData.postValue(Resource.error(msg = e.toString()))
            }
        }
        return _petDetailData
    }

    fun setTimer() {
        countDownTimer = object : CountDownTimer(initialCountDown, countDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                val hours = ((millisUntilFinished / (1000 * 60 * 60)) % 60)
                val minutes = (millisUntilFinished / (1000 * 60) % 60)
                val seconds = (millisUntilFinished / 1000) % 60
                val formatTime = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds)
                _timeCount.postValue(formatTime)
            }

            override fun onFinish() {

            }

        }.start()
    }
}