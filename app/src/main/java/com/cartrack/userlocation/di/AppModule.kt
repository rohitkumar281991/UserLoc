package com.cartrack.userlocation.di

import android.content.Context
import androidx.room.Room
import com.cartrack.userlocation.Constants
import com.cartrack.userlocation.data.database.UserDetailsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Singleton
    @Provides
    fun provideUserDetailsDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        UserDetailsDatabase::class.java,
        Constants.USER_LOCATION_DB
    ).fallbackToDestructiveMigration().build()

}