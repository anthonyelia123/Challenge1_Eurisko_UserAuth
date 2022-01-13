package com.example.eurisko_challenge.Models

import android.content.Context
import androidx.room.Room
import com.example.eurisko_challenge.RoomDatabase.AppDatabase
import com.example.eurisko_challenge.RoomDatabase.ImagesDao
import com.example.eurisko_challenge.RoomDatabase.UsersDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Provides
    fun provideUsersDao(appDatabase: AppDatabase) : UsersDao{
        return appDatabase.userDao()
    }
    @Provides
    fun provideImagesDao(appDatabase : AppDatabase) : ImagesDao {
        return appDatabase.imagesDao()
    }

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext appContext : Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            AppDatabase.DB_NAME
        ).build()
    }

}