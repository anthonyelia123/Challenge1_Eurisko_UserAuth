package com.example.eurisko_challenge.RoomDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Users::class, UsersImages::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UsersDao
    abstract fun imagesDao(): ImagesDao

    companion object {
        const val DB_NAME = "app.db"
    }
}