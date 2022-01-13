package com.example.eurisko_challenge.RoomDatabase

import androidx.room.*

@Dao
interface ImagesDao {
    @Query("SELECT * FROM images WHERE iid = :id LIMIT 1")
    suspend fun findImageById(id: Long): UsersImages?

    @Query("SELECT * FROM images WHERE users_id = :userId LIMIT 1")
    suspend fun findImageByUserId(userId: Long?): UsersImages?


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(userImage: UsersImages): Long

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun update(userImage: UsersImages)
}