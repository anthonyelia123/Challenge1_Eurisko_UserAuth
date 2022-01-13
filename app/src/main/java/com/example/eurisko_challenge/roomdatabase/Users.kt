package com.example.eurisko_challenge.roomdatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.eurisko_challenge.roomdatabase.Users.Companion.TABLE_NAME

@Entity(
    tableName = TABLE_NAME,
    indices = [Index(value = [Users.EMAIL], unique = true)])
data class Users(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "uid") var id: Long = 0,
    @ColumnInfo(name = FIRST_NAME) var firstname: String,
    @ColumnInfo(name = LAST_NAME) var lastname: String,
    @ColumnInfo(name = EMAIL) var email: String,
    @ColumnInfo(name = USER_AUTH_ID) var userAuthId: String
    ) {

    constructor() : this(0L, "", "", "", "")

    companion object {
        const val TABLE_NAME = "users"
        const val FIRST_NAME = "first_name"
        const val LAST_NAME = "last_name"
        const val EMAIL = "email"
        const val USER_AUTH_ID = "user_auth_id"

    }
}