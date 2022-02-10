package com.cartrack.userlocation.viewmodel

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.CheckBox
import androidx.lifecycle.*
import com.cartrack.userlocation.Constants
import com.cartrack.userlocation.SessionManagerUtil
import com.cartrack.userlocation.data.Resource
import com.cartrack.userlocation.data.api.RetroRepository
import com.cartrack.userlocation.data.api.model.UserInfoRepositoryList
import com.cartrack.userlocation.databinding.LoginLayoutBinding
import com.cartrack.userlocation.utils.Utility
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.security.InvalidParameterException
import java.util.concurrent.ExecutionException
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(var retroRepository: RetroRepository) :
    ViewModel() {
    companion object {
        private const val TAG = "Ro_LoginViewModel"
    }

    private val _res = MutableLiveData<Resource<Unit, String>>()
    val res: LiveData<Resource<Unit, String>> = _res

    private val _insertUserDataStatus = MutableLiveData<Resource<Unit, String>>()

    val insertUserDataStatus: LiveData<Resource<Unit, String>> = _insertUserDataStatus
    private var job: Job? = null

    //    fun insertUserData(users: Users) {
//        if (job != null) {
//            job?.cancel()
//        }
//        val exceptionHandler = CoroutineExceptionHandler { _, e ->
//            when (e) {
//                is InvalidParameterException -> {
//                    _insertUsersDataStatus.postValue(Resource.error("invalid params"))
//                }
//                is ExecutionException -> {
//                    Log.e(TAG, "Execution exception ${e.message}")
//                }
//                else -> {
//                    Log.e(TAG, "Unknown exception ${e.message}")
//                }
//            }
//        }
//        job = viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
//            Log.d(TAG, "insert into db")
//            _insertUsersDataStatus.postValue(Resource.loading(null))
//            try {
//                val data = localUserRepositoryInterface.addUser(users)
//                _insertUsersDataStatus.postValue(Resource.success(data))
//            } catch (exception: Exception) {
//                _insertUsersDataStatus.postValue(Resource.error(exception.message!!, null))
//            }
//        }
//    }
    fun insertSingleUserData(user: UserInfoRepositoryList) {
        if (job != null) {
            job?.cancel()
        }
        val exceptionHandler = CoroutineExceptionHandler { _, e ->
            when (e) {
                is InvalidParameterException -> {
                    _insertUserDataStatus.postValue(Resource.error("invalid params"))
                }
                is ExecutionException -> {
                    Log.e(TAG, "Execution exception ${e.message}")
                }
                else -> {
                    Log.e(TAG, "Unknown exception ${e.message}")
                }
            }
        }
        job = viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            Log.d(TAG, "insert into db")
            _insertUserDataStatus.postValue(Resource.loading(null))
            try {
                val data = retroRepository.insertRecord(user)
                _insertUserDataStatus.postValue(Resource.success(data))
            } catch (exception: Exception) {
                _insertUserDataStatus.postValue(Resource.error(exception.message!!, null))
            }
        }
    }

    /**
     * Data coming from Database
     */
    fun getUserList(): LiveData<List<UserInfoRepositoryList>>? {
        var result: LiveData<List<UserInfoRepositoryList>>? = null
        if (job != null) {
            job?.cancel()
        }
        val exceptionHandler = CoroutineExceptionHandler { _, e ->
            when (e) {
                is InvalidParameterException -> {
                    _res.postValue(Resource.error("invalid params"))
                }
                is ExecutionException -> {
                    Log.e(TAG, "Execution exception ${e.message}")
                }
                else -> {
                    Log.e(TAG, "Unknown exception ${e.message}")
                }
            }
        }
        _res.value = Resource.loading(null)
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            Log.d(TAG, "get all records")
            _res.postValue(Resource.loading(null))
            result = retroRepository.getAllRecords()
            _res.postValue(Resource.success())
        }
        return result
    }

    /**
     * Data coming from server call
     */
    fun makeApiCall() {
        CoroutineScope(Dispatchers.IO).launch {
            retroRepository.makeApiCall()
        }
    }

    var jobApiCall: Job? = null
    val usersListFromServer = MutableLiveData<List<UserInfoRepositoryList>>()
    val loading = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<Throwable>()

    /**
     * Data coming from server call
     */
    fun getAllUsersFromServerUsingResponse() {
        val exceptionHandler = CoroutineExceptionHandler { _, e ->
            onError(e)
        }
        jobApiCall = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = retroRepository.getAllUsersDataUsingResponse()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
//                    usersListFromServer.postValue(response.body())
                    loading.value = false
                    insertUserDetails(response)
                }
            }
        }
    }

    fun getAllUsersFromServerUsingCall() {
        val call: Call<List<UserInfoRepositoryList>> = retroRepository.getAllUsersDataUsingCall()
        call.enqueue(object : Callback<List<UserInfoRepositoryList>> {
            override fun onResponse(
                call: Call<List<UserInfoRepositoryList>>,
                response: Response<List<UserInfoRepositoryList>>
            ) {
                if (response.isSuccessful) {
                    usersListFromServer.postValue(response.body())
                    loading.value = false
                    insertUserDetails(response)
                }
            }

            override fun onFailure(call: Call<List<UserInfoRepositoryList>>, t: Throwable) {
                onError(t)
            }

        })
    }


    fun insertUserDetails(
        response: Response<List<UserInfoRepositoryList>>
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            response.body()?.forEach {
                retroRepository.insertRecord(it)
            }
        }
    }

    private fun onError(message: Throwable) {
        errorMessage.postValue(message)
        loading.postValue(false)
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }

    fun validateUserInput(binding: LoginLayoutBinding): Constants.UserValidation {
        return Utility.validation(binding)
    }

    fun hideKeyboard(activity: Activity) {
        Utility.hideKeyboard(activity)
    }

    private fun rememberMe(rememberMeCheckbox: CheckBox): Boolean {
        return rememberMeCheckbox.isChecked
    }

    fun startUserSession(context: Context, binding: LoginLayoutBinding) {
        if (rememberMe(binding.rememberMeCheckbox)) {
            SessionManagerUtil.startUserSession(context)
        }
    }
}