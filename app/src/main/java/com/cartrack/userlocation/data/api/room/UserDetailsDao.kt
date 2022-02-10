package com.cartrack.userlocation.data.api.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cartrack.userlocation.data.api.model.UserInfoRepositoryList

@Dao
interface UserDetailsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserDetails(usersDetails: UserInfoRepositoryList)

    @Query("SELECT * FROM USER_DETAIL_TABLE")
    fun getUserDetailsList(): LiveData<List<UserInfoRepositoryList>>


    @Query("SELECT * FROM USER_DETAIL_TABLE")
    fun getUserDetailsList1(): List<UserInfoRepositoryList>


    @Query("DELETE FROM USER_DETAIL_TABLE")
    suspend fun deleteAllUserDetails()
}