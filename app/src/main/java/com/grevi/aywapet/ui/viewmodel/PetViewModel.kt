package com.grevi.aywapet.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grevi.aywapet.datasource.response.PetDetailResponse
import com.grevi.aywapet.datasource.response.PetResponse
import com.grevi.aywapet.repository.RepositoryImpl
import com.grevi.aywapet.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PetViewModel @Inject constructor(private val repositoryImpl: RepositoryImpl) : ViewModel() {

    private val _petData = MutableLiveData<Resource<PetResponse>>()
    private val _petDetailData = MutableLiveData<Resource<PetDetailResponse>>()
    val petData : MutableLiveData<Resource<PetResponse>> get() = _petData

    init {
        getAllPet()
    }

    private fun getAllPet() : LiveData<Resource<PetResponse>> {
        viewModelScope.launch {
            val data = repositoryImpl.getAllPet()
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

    internal fun getPet(id : String) : LiveData<Resource<PetDetailResponse>> {
        viewModelScope.launch {
            val data = repositoryImpl.getPet(id)
            _petDetailData.postValue(Resource.loading(msg = "Memuat data..."))
            try {
                data.data?.let { _petDetailData.postValue(Resource.success(data = it)) }
            } catch (e : Exception) {
                _petDetailData.postValue(Resource.error(msg = e.toString()))
            }
        }
        return _petDetailData
    }

}