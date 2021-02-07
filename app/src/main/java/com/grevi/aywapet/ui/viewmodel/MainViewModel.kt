package com.grevi.aywapet.ui.viewmodel

import android.os.CountDownTimer
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.*
import com.grevi.aywapet.datasource.response.*
import com.grevi.aywapet.db.entity.Users
import com.grevi.aywapet.repository.RemoteRepos
import com.grevi.aywapet.utils.Constant.HOUR_KEY
import com.grevi.aywapet.utils.Constant.MINUTES_KEY
import com.grevi.aywapet.utils.Constant.SECONDS_KEY
import com.grevi.aywapet.utils.Constant.TIMER_KEY
import com.grevi.aywapet.utils.Constant.TIMER_WORKER_TAG
import com.grevi.aywapet.utils.Resource
import com.grevi.aywapet.utils.State
import com.grevi.aywapet.worker.TimerWorker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

class MainViewModel @ViewModelInject constructor(private val remoteRepos: RemoteRepos, private val workManager: WorkManager) : ViewModel() {
    private val _petData = MutableLiveData<Resource<PetResponse>>()
    private val _animalData = MutableLiveData<Resource<AnimalResponse>>()
    private val _petDetailData = MutableLiveData<Resource<PetDetailResponse>>()
    private val _emailData = MutableLiveData<Resource<VerifyResponse>>()

    private val _keepDataResp = MutableLiveData<Resource<GetKeepResponse>>()
    private val _keepPostData = MutableLiveData<Resource<KeepPostResponse>>()
    private val _keepSuccessData = MutableLiveData<Resource<GetKeepSuccessResponse>>()
    private val _usersLocalFlow = MutableStateFlow<State<MutableList<Users>>>(State.Data)

    private lateinit var countDownTimer: CountDownTimer
    private val initialCountDown : Long = 86400000 //24hours
    private val countDownInterval : Long = 1000
    private val _timeCount : MutableLiveData<String> = MutableLiveData()

    val petData : MutableLiveData<Resource<PetResponse>> get() = _petData
    val animalData : MutableLiveData<Resource<AnimalResponse>> get() = _animalData
    val timeCount : MutableLiveData<String> get() = _timeCount
    val keepData : MutableLiveData<Resource<GetKeepResponse>> = _keepDataResp
    val keepSuccessData : MutableLiveData<Resource<GetKeepSuccessResponse>> = _keepSuccessData
    val outputWorkInfo : LiveData<List<WorkInfo>>
    val getUserLocalFlow : MutableStateFlow<State<MutableList<Users>>> get() = _usersLocalFlow


    init {
        getAllPet()
        getAnimal()
        getKeepPet()
        getKeepSuccessKeep()
        getLocalUser()
        outputWorkInfo = workManager.getWorkInfosByTagLiveData(TIMER_WORKER_TAG)
    }

    private fun getLocalUser() {
        viewModelScope.launch {
            remoteRepos.getLocalUser().collect {
                _usersLocalFlow.value = State.Loading()
                try {
                    _usersLocalFlow.value = State.Success(it)
                }catch (e : Exception) {
                    _usersLocalFlow.value = State.Error(e)
                }
            }
        }
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
            remoteRepos.getLocalUser().collect { data ->
                data.map {
                    val dataRemote = remoteRepos.getKeepSuccess(it.id)
                    _keepSuccessData.postValue(Resource.loading(msg = "Load"))
                    try {
                        _keepSuccessData.postValue(Resource.success(data = dataRemote.data!!))
                    } catch (e : Exception) {
                        _keepSuccessData.postValue(Resource.error(msg = e.toString()))
                    }
                }
            }
        }
        return _keepSuccessData
    }

    internal fun keepPostData(idPet : String) : LiveData<Resource<KeepPostResponse>> {
        viewModelScope.launch {
            remoteRepos.getLocalUser().collect { data ->
                data.map {
                    val dataRemote = remoteRepos.keepPost(idPet, it.id)
                    _keepPostData.postValue(Resource.loading(msg = "Load"))
                    try {
                        _keepPostData.postValue(Resource.success(data = dataRemote.data!!))
                    } catch (e : Exception) {
                        _keepPostData.postValue(Resource.error(msg = e.toString()))
                    }
                }
            }
        }
        return _keepPostData
    }

    private fun getKeepPet() : LiveData<Resource<GetKeepResponse>> {
        viewModelScope.launch {
            remoteRepos.getLocalUser().collect { data ->
                data.map {
                    val dataRemote = remoteRepos.getKeepPet(it.id)
                    _keepDataResp.postValue(Resource.loading(msg = "Load"))
                    try {
                        _keepDataResp.postValue(Resource.success(data = dataRemote.data!!))
                    } catch (e : Exception) {
                        _keepDataResp.postValue(Resource.error(msg = e.toString()))
                    }
                }
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

    fun setWorker() {
        val data = Data.Builder().let {
            it.putInt(TIMER_KEY, 100)
            it.build()
        }

        val timerWorker = OneTimeWorkRequestBuilder<TimerWorker>()
                .addTag(TIMER_WORKER_TAG)
                .setInputData(data)
                .build()

        workManager.enqueue(timerWorker)
    }

    fun setTimer() {
        countDownTimer = object : CountDownTimer(initialCountDown, countDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                val hours = ((millisUntilFinished / (1000 * 60 * 60)) % 60)
                val minutes = (millisUntilFinished / (1000 * 60) % 60)
                val seconds = (millisUntilFinished / 1000) % 60
                val formatTime = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds)

                val data = Data.Builder().let {
                    it.putLong(SECONDS_KEY, seconds)
                    it.putLong(MINUTES_KEY, minutes)
                    it.putLong(HOUR_KEY, hours)
                    it.build()
                }

                val timerWorker = OneTimeWorkRequestBuilder<TimerWorker>()
                        .addTag(TIMER_WORKER_TAG)
                        .setInputData(data)
                        .build()
                workManager.enqueue(timerWorker)
            }

            override fun onFinish() {

            }

        }.start()
    }
}