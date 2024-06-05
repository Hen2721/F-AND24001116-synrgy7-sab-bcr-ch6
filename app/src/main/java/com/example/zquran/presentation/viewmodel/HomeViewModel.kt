package com.example.zquran.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.LocalRepository
import com.example.domain.Surah
import com.example.domain.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val localRepository: LocalRepository
): ViewModel() {
    private val _surahData: MutableLiveData<List<Surah>> = MutableLiveData()
    val surahData: LiveData<List<Surah>> = _surahData

    private val _loading: MutableLiveData<Boolean> = MutableLiveData()
    val loading: LiveData<Boolean> = _loading

    private val _logoutLoading: MutableLiveData<Boolean> = MutableLiveData()
    val logoutLoading: LiveData<Boolean> = _logoutLoading

    private val _error: MutableLiveData<String> = MutableLiveData()
    val error: LiveData<String> = _error

    fun fetchAllSurahData() {
        _loading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                mainRepository.fetchAllSurahData()
            }.onFailure { exception ->
                withContext(Dispatchers.Main) {
                    _loading.value = false
                    _error.value = exception.message
                }
            }.onSuccess { surah ->
                withContext(Dispatchers.Main) {
                    _loading.value = false
                    _surahData.value = surah
                }
            }
        }
    }

    fun logout() {
        _logoutLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            delay(1000)
            localRepository.clearToken()
            withContext(Dispatchers.Main) {
                _logoutLoading.value = false
            }
        }
    }
}