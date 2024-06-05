package com.example.zquran.presentation.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkInfo
import com.example.data.local.LocalRepository
import com.example.domain.Profile
import com.example.domain.repository.MainRepository
import com.example.presentation.usecase.ApplyBlurUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val localRepository: LocalRepository,
    private val applyBlurUseCase: ApplyBlurUseCase,
): ViewModel() {
    private val _profileData: MutableLiveData<Profile> = MutableLiveData()
    val profileData: LiveData<Profile> = _profileData

    private val _loading: MutableLiveData<Boolean> = MutableLiveData()
    val loading: LiveData<Boolean> = _loading

    private val _dataLoading: MutableLiveData<Boolean> = MutableLiveData()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _logoutLoading: MutableLiveData<Boolean> = MutableLiveData()
    val logoutLoading: LiveData<Boolean> = _logoutLoading

    private val _profileImageLoading: MutableLiveData<Boolean> = MutableLiveData()
    val profileImageLoading: LiveData<Boolean> = _profileImageLoading

    private val _message: MutableLiveData<String> = MutableLiveData()
    val message: LiveData<String> = _message

    private val _profileImageMessage: MutableLiveData<String> = MutableLiveData()
    val profileImageMessage: LiveData<String> = _profileImageMessage

    private val _profileImage: MutableLiveData<String> = MutableLiveData()
    val profileImage: LiveData<String> = _profileImage

    private val _error: MutableLiveData<String> = MutableLiveData()
    val error: LiveData<String> = _error

    fun getProfile() {
        _dataLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                mainRepository.fetchProfile(localRepository.getToken().first()!!)
            }.onFailure { exception ->
                withContext(Dispatchers.Main) {
                    _dataLoading.value = false
                    _error.value = exception.message
                }
            }.onSuccess { profile ->
                withContext(Dispatchers.Main) {
                    _dataLoading.value = false
                    _profileData.value = profile
                }
            }
        }
    }

    fun updateProfile(user: Profile) {
        _loading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            if (localRepository.validateProfileInput(user)) {
                runCatching {
                    mainRepository.updateProfile(localRepository.getToken().first()!!, user)
                }.onFailure { exception ->
                    withContext(Dispatchers.Main) {
                        _loading.value = false
                        _error.value = exception.message
                    }
                }.onSuccess { profile ->
                    withContext(Dispatchers.Main) {
                        _loading.value = false
                        _message.value = profile
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    _error.value = "Field tidak valid!"
                    _loading.value = false
                }
            }
        }
    }

    fun updateProfileImage(image: MultipartBody.Part) {
        _profileImageLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            if (localRepository.validateProfileImage(image)) {
                runCatching {
                    mainRepository.uploadProfileImage(localRepository.getToken().first()!!, image)
                }.onFailure { exception ->
                    withContext(Dispatchers.Main) {
                        _profileImageLoading.value = false
                        _error.value = exception.message
                    }
                }.onSuccess { profile ->
                    withContext(Dispatchers.Main) {
                        _profileImageLoading.value = false
                        _profileImageMessage.value = profile
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    _error.value = "Field tidak valid!"
                    _profileImageLoading.value = false
                }
            }
        }
    }

    fun applyBlurImage(profileImage: Uri) {
        applyBlurUseCase.invoke(profileImage)
    }

    fun saveBlurImage(profileImage: String) {
        viewModelScope.launch(Dispatchers.IO) {
            localRepository.saveProfileImage(profileImage)

            localRepository.loadProfileImage()
                .catch { throwable ->
                    withContext(Dispatchers.Main) {
                        _error.value = throwable.message
                    }
                }
                .collectLatest { profileImage ->
                    withContext(Dispatchers.Main) {
                        _profileImage.value = profileImage
                    }
                }
        }
    }

    fun getOutputWorkerInfo(): LiveData<List<WorkInfo>> {
        return applyBlurUseCase.getWorkManagerLiveData()
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