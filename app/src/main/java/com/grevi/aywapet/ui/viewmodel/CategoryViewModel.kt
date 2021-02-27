package com.grevi.aywapet.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grevi.aywapet.datasource.response.AnimalResponse
import com.grevi.aywapet.datasource.response.PetResponse
import com.grevi.aywapet.repository.RepositoryImpl
import com.grevi.aywapet.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(private val repositoryImpl: RepositoryImpl) : ViewModel() {

    private val _petData = MutableLiveData<Resource<PetResponse>>()
    private val _animalData = MutableLiveData<Resource<AnimalResponse>>()

    val animalData : MutableLiveData<Resource<AnimalResponse>> get() = _animalData


    init {
        getAnimal()
    }

    internal fun getPetByType(idType : String) : LiveData<Resource<PetResponse>> {
        viewModelScope.launch {
            val data = repositoryImpl.getPetByType(idType)
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
            val data = repositoryImpl.getAnimal()
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
}