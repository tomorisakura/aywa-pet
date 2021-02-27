package com.grevi.aywapet.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grevi.aywapet.db.entity.Users
import com.grevi.aywapet.repository.RepositoryImpl
import com.grevi.aywapet.utils.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val repositoryImpl: RepositoryImpl) : ViewModel() {

    private val _usersLocalFlow = MutableStateFlow<State<MutableList<Users>>>(State.Data)
    val getUserLocalFlow : MutableStateFlow<State<MutableList<Users>>> get() = _usersLocalFlow

    init {
        getLocalUser()
    }

    private fun getLocalUser() {
        viewModelScope.launch {
            repositoryImpl.getLocalUser().collect {
                _usersLocalFlow.value = State.Loading()
                try {
                    _usersLocalFlow.value = State.Success(it)
                }catch (e : Exception) {
                    _usersLocalFlow.value = State.Error(e)
                }
            }
        }
    }
}