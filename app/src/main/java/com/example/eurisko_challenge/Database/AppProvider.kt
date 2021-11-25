package com.example.eurisko_challenge.Database

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri
import android.util.Log
import com.example.eurisko_challenge.Objects.User
import com.example.eurisko_challenge.Objects.UsersImage

private const val TAG = "AppProvider"

const val CONTENT_AUTHORITY = "eurisko.mobility.challenge.provider"

private const val USERS = 100
private const val USER_ID = 101

private const val USERSIMAGE = 200
private const val USERSIMAGE_ID = 201


val CONTENT_AUTHORITY_URI: Uri = Uri.parse("content://$CONTENT_AUTHORITY")

internal class AppProvider: ContentProvider() {

    private val uriMatcher by lazy { buildUriMatcher() }

    private fun buildUriMatcher() : UriMatcher {
        val matcher = UriMatcher(UriMatcher.NO_MATCH)

        // e.g. content://com.example.euriko_challenge.challenge.provider/Users
        matcher.addURI(CONTENT_AUTHORITY, User.TABLE_NAME, USERS);

        // e.g. content://com.example.euriko_challenge.challenge.provider/Users/8
        matcher.addURI(CONTENT_AUTHORITY, "${User.TABLE_NAME}/#", USER_ID)

        // e.g. content://com.example.euriko_challenge.challenge.provider/UsersImage
        matcher.addURI(CONTENT_AUTHORITY, UsersImage.TABLE_NAME, USERSIMAGE);

        return matcher
    }

    override fun onCreate(): Boolean {
        Log.d(TAG, "onCreate: starts")
        return true
    }

    override fun getType(uri: Uri): String {
        val match = uriMatcher.match(uri)

        return when (match) {
            USERS -> User.CONTENT_TYPE

            USER_ID -> User.CONTENT_ITEM_TYPE

            else -> throw IllegalArgumentException("unknown Uri: $uri")
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val match = uriMatcher.match(uri)

        val recordId: Long
        val returnUri: Uri

        when(match) {

            USERS -> {
                val db = AppDatabase.getInstance(context!!).writableDatabase
                recordId = db.insert(User.TABLE_NAME, null, values)
                if(recordId != -1L) {
                    //returnUri = User.buildUriFromId(1)
                } else {
                    throw SQLException("Failed to insert, Uri was $uri")
                }
            }
            USERSIMAGE -> {
                val db = AppDatabase.getInstance(context!!).writableDatabase
                recordId = db.insert(UsersImage.TABLE_NAME, null, values)
            }

            else -> throw IllegalArgumentException("Unknown uri: $uri")
        }
        return null
    }


    override fun query(uri: Uri, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor {
        val match = uriMatcher.match(uri)
        Log.d(TAG, "query: match is $match")

        val queryBuilder = SQLiteQueryBuilder()

        when (match) {
            USERS -> queryBuilder.tables = User.TABLE_NAME

            USER_ID -> {
                queryBuilder.tables = User.TABLE_NAME
                val userId = User.getId(uri)
                queryBuilder.appendWhere("${User.Columns.ID} = ")
                queryBuilder.appendWhereEscapeString("$userId")
            }
            USERSIMAGE -> {
                queryBuilder.tables = UsersImage.TABLE_NAME
            }

            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }

        val db = AppDatabase.getInstance(context!!).readableDatabase
        val cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder)

        return cursor
    }



    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        val match = uriMatcher.match(uri)


        when(match) {

            USERS -> {
                val db = AppDatabase.getInstance(context!!).writableDatabase
                db.update(User.TABLE_NAME, values, selection, selectionArgs)
            }
            USERSIMAGE -> {
                val db = AppDatabase.getInstance(context!!).writableDatabase
                db.update(UsersImage.TABLE_NAME, values, selection, selectionArgs)
            }

            else -> throw IllegalArgumentException("Unknown uri: $uri")
        }
        return 0
    }

}