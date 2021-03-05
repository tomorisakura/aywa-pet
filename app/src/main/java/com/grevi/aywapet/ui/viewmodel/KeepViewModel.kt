package com.grevi.aywapet.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grevi.aywapet.datasource.response.GetKeepResponse
import com.grevi.aywapet.datasource.response.GetKeepSuccessResponse
import com.grevi.aywapet.datasource.response.KeepPostResponse
import com.grevi.aywapet.repository.RepositoryImpl
import com.grevi.aywapet.utils.ApiException
import com.grevi.aywapet.utils.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class KeepViewModel @Inject constructor(private val repositoryImpl: RepositoryImpl) : ViewModel() {

    private val _keepDataResp = MutableLiveData<State<GetKeepResponse>>()
    private val _keepPostData = MutableLiveData<State<KeepPostResponse>>()
    private val _keepSuccessData = MutableLiveData<State<GetKeepSuccessResponse>>()

    val keepData : MutableLiveData<State<GetKeepResponse>> get() =  _keepDataResp
    val keepSuccessData : MutableLiveData<State<GetKeepSuccessResponse>> get() = _keepSuccessData

    init {
        getKeepPet()
        getKeepSuccessKeep()
    }

    internal fun keepPostData(idPet : String) : LiveData<State<KeepPostResponse>> {
        viewModelScope.launch {
            repositoryImpl.getLocalUser().collect { data ->
                data.map {
                    val dataRemote = repositoryImpl.keepPost(idPet, it.id)
                    _keepPostData.postValue(State.Loading())
                    try {
                        _keepPostData.postValue(dataRemote)
                    } catch (e : ApiException) {
                        _keepPostData.postValue(State.Error(e))
                    }
                }
            }
        }
        return _keepPostData
    }

    private fun getKeepPet() : LiveData<State<GetKeepResponse>> {
        viewModelScope.launch {
            repositoryImpl.getLocalUser().collect { data ->
                data.map {
                    val dataRemote = repositoryImpl.getKeepPet(it.id)
                    _keepDataResp.postValue(State.Loading())
                    try {
                        _keepDataResp.postValue(dataRemote)
                    } catch (e : ApiException) {
                        _keepDataResp.postValue(State.Error(e))
                    }
                }
            }
        }
        return _keepDataResp
    }

    private fun getKeepSuccessKeep() : LiveData<State<GetKeepSuccessResponse>> {
        viewModelScope.launch {
            repositoryImpl.getLocalUser().collect { data ->
                data.map {
                    val dataRemote = repositoryImpl.getKeepSuccess(it.id)
                    _keepSuccessData.postValue(State.Loading())
                    try {
                        _keepSuccessData.postValue(dataRemote)
                    } catch (e : Exception) {
                        _keepSuccessData.postValue(State.Error(e))
                    }
                }
            }
        }
        return _keepSuccessData
    }

}