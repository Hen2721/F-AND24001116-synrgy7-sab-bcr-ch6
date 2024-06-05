package com.example.zquran.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.SurahDetail
import com.example.domain.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SurahDetailViewModel @Inject constructor(
    private val mainRepository: MainRepository
): ViewModel() {
    private val _surahDetailData: MutableLiveData<SurahDetail> = MutableLiveData()
    val surahDetailData: LiveData<SurahDetail> = _surahDetailData

    private val _loading: MutableLiveData<Boolean> = MutableLiveData()
    val loading: LiveData<Boolean> = _loading

    private val _error: MutableLiveData<String> = MutableLiveData()
    val error: LiveData<String> = _error

    fun fetchSurahDataDetail(noSurah: Int) {
        _loading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                mainRepository.fetchSurahDetail(noSurah)
            }.onFailure { exception ->
                withContext(Dispatchers.Main) {
                    _loading.value = false
                    _error.value = exception.message
                }
            }.onSuccess { surah ->
                withContext(Dispatchers.Main) {
                    _loading.value = false
                    _surahDetailData.value = surah
                }
            }
        }
    }
}