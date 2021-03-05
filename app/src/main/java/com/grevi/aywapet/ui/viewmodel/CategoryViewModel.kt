package com.grevi.aywapet.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grevi.aywapet.datasource.response.AnimalResponse
import com.grevi.aywapet.datasource.response.PetResponse
import com.grevi.aywapet.repository.RepositoryImpl
import com.grevi.aywapet.utils.ApiException
import com.grevi.aywapet.utils.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(private val repositoryImpl: RepositoryImpl) : ViewModel() {

    private val _petData = MutableLiveData<State<PetResponse>>()
    private val _animalData = MutableLiveData<State<AnimalResponse>>()

    val animalData : MutableLiveData<State<AnimalResponse>> get() = _animalData


    init {
        getAnimal()
    }

    internal fun getPetByType(idType : String) : LiveData<State<PetResponse>> {
        viewModelScope.launch {
            val data = repositoryImpl.getPetByType(idType)
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

    private fun getAnimal() : LiveData<State<AnimalResponse>> {
        viewModelScope.launch {
            val data = repositoryImpl.getAnimal()
            _animalData.postValue(State.Loading())
            try {
                _animalData.postValue(data)
            }catch (e : ApiException) {
                e.printStackTrace()
                _animalData.postValue(State.Error(e))
            }
        }
        return _animalData
    }
}