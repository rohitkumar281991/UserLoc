package com.cartrack.userlocation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cartrack.userlocation.data.api.RetroRepository
import com.cartrack.userlocation.data.api.model.LocationModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserLocationViewModel @Inject constructor(var retroRepository: RetroRepository) :
    ViewModel() {
    companion object {
        private const val TAG = "Ro_UserLocationViewModel"
    }

    val mutableLiveData = MutableLiveData<RetroRepository>()

    fun sendData(retroRepository: RetroRepository) {
        mutableLiveData.postValue(retroRepository)
    }

    val locationChanged = MutableLiveData<LocationModel>()

    fun udpateLocation(model: LocationModel){
        locationChanged.postValue(model)
    }
}
