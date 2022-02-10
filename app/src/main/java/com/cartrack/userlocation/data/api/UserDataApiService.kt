package com.cartrack.userlocation.data.api

import com.cartrack.userlocation.data.api.model.UserInfoRepositoryList
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface UserDataApiService {
    @GET("users")
    fun getDataFromApi(): Call<List<UserInfoRepositoryList>>

    @GET("users")
    fun getDataFromApiUsingResponse(): Response<List<UserInfoRepositoryList>>
}