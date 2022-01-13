package com.example.eurisko_challenge.roomdatabase

import androidx.room.*
import com.example.eurisko_challenge.roomdatabase.UsersImages.Companion.IMAGES
import com.example.eurisko_challenge.roomdatabase.UsersImages.Companion.USERS_ID

@Entity(
    tableName = UsersImages.TABLE_NAME,
    foreignKeys = [ForeignKey(
        entity = Users::class,
        parentColumns = ["uid"],
        childColumns = [USERS_ID],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(IMAGES), Index(USERS_ID)]
)
data class UsersImages(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "iid") var id: Long = 0,
    @ColumnInfo(name = IMAGES) var image: ByteArray? ,
    @ColumnInfo(name = USERS_ID) var userId: Long) {

constructor() : this(0L, null, 0L)
    companion object {
        const val TABLE_NAME = "images"
        const val IMAGES = "images"
        const val USERS_ID = "users_id"
    }
}