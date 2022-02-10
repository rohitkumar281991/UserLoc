package com.cartrack.userlocation.data.api

import android.util.Log
import androidx.lifecycle.LiveData
import com.cartrack.userlocation.data.api.model.UserInfoRepositoryList
import com.cartrack.userlocation.data.api.room.UserDetailsDao
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class RetroRepository @Inject constructor(
    private val userDataApiService: UserDataApiService,
    private val userDetailsDao: UserDetailsDao
) {
    fun getAllRecords(): LiveData<List<UserInfoRepositoryList>> {
        return userDetailsDao.getUserDetailsList()
    }

    suspend fun insertRecord(userInfo: UserInfoRepositoryList) {
        Log.d("rohit ", "inserting data -----> " + userInfo.name)
        userDetailsDao.insertUserDetails(userInfo)
    }

    fun getAllRecords1(): List<UserInfoRepositoryList> {
        return userDetailsDao.getUserDetailsList1()
    }

    fun makeApiCall(): Boolean {
        var result = false
        val call: Call<List<UserInfoRepositoryList>> = userDataApiService.getDataFromApi()
        call.enqueue(object : Callback<List<UserInfoRepositoryList>> {
            override fun onResponse(
                call: Call<List<UserInfoRepositoryList>>,
                response: Response<List<UserInfoRepositoryList>>
            ) {
                if (response.isSuccessful) {
                    result = true
                    Log.d("rohit ", "response " + response.body()?.get(0)?.name)
                }
            }

            override fun onFailure(call: Call<List<UserInfoRepositoryList>>, t: Throwable) {
                result = false
            }
        })
        return result
    }

    suspend fun deleteAllRecords() {
        userDetailsDao.deleteAllUserDetails()
    }

    fun getAllUsersDataUsingCall(): Call<List<UserInfoRepositoryList>> {
        return userDataApiService.getDataFromApi()
    }

    fun getAllUsersDataUsingResponse(): Response<List<UserInfoRepositoryList>> {
        return userDataApiService.getDataFromApiUsingResponse()
    }

}
