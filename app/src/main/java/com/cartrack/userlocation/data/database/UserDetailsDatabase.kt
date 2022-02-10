package com.cartrack.userlocation.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.cartrack.userlocation.data.api.model.*
import com.cartrack.userlocation.data.api.room.UserDetailsDao

@Database(entities = [UserInfoRepositoryList::class],version = 1,exportSchema = false)
@TypeConverters(TypeConverterAddress::class,TypeConverterCompany::class,TypeConverterLocation::class)
abstract class UserDetailsDatabase : RoomDatabase() {
    abstract val userDetailsDao : UserDetailsDao
}