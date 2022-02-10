package com.cartrack.userlocation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cartrack.userlocation.data.api.RetroRepository
import com.cartrack.userlocation.data.api.model.UserInfoRepositoryList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import javax.inject.Inject

@HiltViewModel
class UserDetailViewModel @Inject constructor(var retroRepository: RetroRepository) :
    ViewModel() {
    companion object {
        private const val TAG = "Ro_UserDetailViewModel"
    }

    private var job: Job? = null
    var allWords: LiveData<List<UserInfoRepositoryList>> = retroRepository.getAllRecords()
    var jobApiCall: Job? = null
    val loading = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
        jobApiCall?.cancel()
    }
}