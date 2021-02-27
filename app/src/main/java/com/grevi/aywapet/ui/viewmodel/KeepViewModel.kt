package com.grevi.aywapet.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grevi.aywapet.datasource.response.GetKeepResponse
import com.grevi.aywapet.datasource.response.GetKeepSuccessResponse
import com.grevi.aywapet.datasource.response.KeepPostResponse
import com.grevi.aywapet.repository.RepositoryImpl
import com.grevi.aywapet.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class KeepViewModel @Inject constructor(private val repositoryImpl: RepositoryImpl) : ViewModel() {

    private val _keepDataResp = MutableLiveData<Resource<GetKeepResponse>>()
    private val _keepPostData = MutableLiveData<Resource<KeepPostResponse>>()
    private val _keepSuccessData = MutableLiveData<Resource<GetKeepSuccessResponse>>()

    val keepData : MutableLiveData<Resource<GetKeepResponse>> get() =  _keepDataResp
    val keepSuccessData : MutableLiveData<Resource<GetKeepSuccessResponse>> get() = _keepSuccessData

    init {
        getKeepPet()
        getKeepSuccessKeep()
    }

    internal fun keepPostData(idPet : String) : LiveData<Resource<KeepPostResponse>> {
        viewModelScope.launch {
            repositoryImpl.getLocalUser().collect { data ->
                data.map {
                    val dataRemote = repositoryImpl.keepPost(idPet, it.id)
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
            repositoryImpl.getLocalUser().collect { data ->
                data.map {
                    val dataRemote = repositoryImpl.getKeepPet(it.id)
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

    private fun getKeepSuccessKeep() : LiveData<Resource<GetKeepSuccessResponse>> {
        viewModelScope.launch {
            repositoryImpl.getLocalUser().collect { data ->
                data.map {
                    val dataRemote = repositoryImpl.getKeepSuccess(it.id)
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

}