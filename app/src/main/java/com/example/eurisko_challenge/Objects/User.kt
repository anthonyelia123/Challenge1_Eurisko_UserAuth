package com.example.eurisko_challenge.Objects

import android.content.ContentUris
import android.net.Uri
import android.provider.BaseColumns
import com.example.eurisko_challenge.Database.CONTENT_AUTHORITY
import com.example.eurisko_challenge.Database.CONTENT_AUTHORITY_URI

object User {

    internal const val TABLE_NAME = "Users"
    val CONTENT_URI: Uri = Uri.withAppendedPath(CONTENT_AUTHORITY_URI, TABLE_NAME)

    const val CONTENT_TYPE = "vnd.android.cursor.dir/vnd.$CONTENT_AUTHORITY.$TABLE_NAME"
    const val CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.$CONTENT_AUTHORITY.$TABLE_NAME"

    object Columns {
        const val ID = BaseColumns._ID
        const val User_FirstName = "firstname"
        const val User_LastName = "lastname"
    }
    fun getId(uri: Uri): String {
        return ContentUris.parseId(uri).toString()
    }


}