package com.example.eurisko_challenge.roomdatabase

import androidx.room.*


@Dao
interface UsersDao {
    @Query("SELECT * FROM users WHERE uid = :id LIMIT 1")
    suspend fun findUserById(id: Long): Users?

    @Query("SELECT * FROM users WHERE user_auth_id = :authId LIMIT 1")
    suspend fun findUserByAuthId(authId: String?): Users?

    @Query("SELECT * FROM users WHERE uid = :id LIMIT 1")
    suspend fun findEmailById(id: Long?): Users?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: Users): Long

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun update(user: Users)
}