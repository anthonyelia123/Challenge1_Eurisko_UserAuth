package com.example.eurisko_challenge.Database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.eurisko_challenge.Objects.User
import com.example.eurisko_challenge.Objects.UsersImage
import learnprogramming.academy.tasktimer.SingletonHolder

private const val TAG = "AppDatabase"

private const val DATABASE_NAME = "EuriskoChallenge.db"
private const val DATABASE_VERSION = 1

internal class AppDatabase private constructor(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        // CREATE TABLE User (_id TEXT PRIMARY KEY NOT NULL, firstname TEXT, lastname TEXT);
        val sSQL = """CREATE TABLE ${User.TABLE_NAME} (
            ${User.Columns.ID} TEXT PRIMARY KEY NOT NULL,
            ${User.Columns.User_FirstName} TEXT,
            ${User.Columns.User_LastName} TEXT);""".replaceIndent(" ")
        Log.d(TAG, sSQL)
        db.execSQL(sSQL)


        // CREATE TABLE UsersImage (_id TEXT PRIMARY KEY NOT NULL, image BLOB);
        val sSQL2 = """CREATE TABLE ${UsersImage.TABLE_NAME} (
            ${UsersImage.Columns.ID} TEXT PRIMARY KEY NOT NULL,
            ${UsersImage.Columns.Image} BLOB);""".replaceIndent(" ")
        Log.d(TAG, sSQL2)
        db.execSQL(sSQL2)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        when(oldVersion) {
            1 -> {
                // upgrade logic from version 1
            }
            else -> throw IllegalStateException("onUpgrade() with unknown newVersion: $newVersion")
        }
    }
    companion object : SingletonHolder<AppDatabase, Context>(::AppDatabase)
}