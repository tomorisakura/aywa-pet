package com.grevi.aywapet.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grevi.aywapet.datasource.response.PetDetailResponse
import com.grevi.aywapet.datasource.response.PetResponse
import com.grevi.aywapet.repository.RepositoryImpl
import com.grevi.aywapet.utils.ApiException
import com.grevi.aywapet.utils.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PetViewModel @Inject constructor(private val repositoryImpl: RepositoryImpl) : ViewModel() {

    private val _petData = MutableLiveData<State<PetResponse>>()
    private val _petDetailData = MutableLiveData<State<PetDetailResponse>>()
    val petData : MutableLiveData<State<PetResponse>> get() = _petData

    init {
        getAllPet()
    }

    private fun getAllPet() : LiveData<State<PetResponse>> {
        viewModelScope.launch {
            val data = repositoryImpl.getAllPet()
            _petData.postValue(State.Loading())
            try {
                _petData.postValue(data)
            } catch (e : ApiException) {
                e.printStackTrace()
                _petData.postValue(State.Error(e))
            }
        }
        return _petData
    }

    internal fun getPet(id : String) : LiveData<State<PetDetailResponse>> {
        viewModelScope.launch {
            val data = repositoryImpl.getPet(id)
            _petDetailData.postValue(State.Loading())
            try {
                _petDetailData.postValue(data)
            } catch (e : ApiException) {
                _petDetailData.postValue(State.Error(e))
            }
        }
        return _petDetailData
    }

    fun getSearchPet(ras : String) : LiveData<State<PetResponse>> {
        viewModelScope.launch {
            val data = repositoryImpl.getSearchPet(ras)
            _petData.postValue(State.Loading())
            try {
                _petData.postValue(data)
            } catch (e : ApiException) {
                e.printStackTrace()
                _petData.postValue(State.Error(e))
            }
        }
        return _petData
    }

}