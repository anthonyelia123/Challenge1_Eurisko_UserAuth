package com.example.eurisko_challenge.repositories

import com.example.eurisko_challenge.roomdatabase.ImagesDao
import com.example.eurisko_challenge.roomdatabase.Users
import com.example.eurisko_challenge.roomdatabase.UsersDao
import com.example.eurisko_challenge.roomdatabase.UsersImages
import javax.inject.Inject

class Repository @Inject constructor(
    private val usersDao: UsersDao,
    private val imagesDao: ImagesDao
) {

    suspend fun findUserById(id : Long) = usersDao.findUserById(id)
    suspend fun findUserByAuthId(authId : String) = usersDao.findUserByAuthId(authId)
    suspend fun findEmailById(id : Long) = usersDao.findEmailById(id)
    suspend fun insert(user: Users) = usersDao.insert(user)
    suspend fun update(user: Users) = usersDao.update(user)

    suspend fun findImageById(id: Long) = imagesDao.findImageById(id)
    suspend fun findImageByUserId(userId: Long?) = imagesDao.findImageByUserId(userId)
    suspend fun insert(userImage: UsersImages) = imagesDao.insert(userImage)
    suspend fun update(userImage: UsersImages) = imagesDao.update(userImage)
}