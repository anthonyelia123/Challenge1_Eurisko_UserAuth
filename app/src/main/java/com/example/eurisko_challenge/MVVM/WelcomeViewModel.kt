package com.example.eurisko_challenge.MVVM

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.eurisko_challenge.RoomDatabase.AppDatabase
import com.example.eurisko_challenge.RoomDatabase.ImagesDao
import com.example.eurisko_challenge.RoomDatabase.Users
import com.example.eurisko_challenge.RoomDatabase.UsersDao
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.DisposableHandle
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor( private val repository: Repository) : ViewModel()  {


    private val mUser : MutableLiveData<Users> = MutableLiveData<Users>()
    private val imagebitmap = MutableLiveData<Bitmap>()
    fun getimage() : LiveData<Bitmap>{
        return imagebitmap
    }
    fun getmUser() : LiveData<Users>{
        return mUser
    }

    suspend fun getUser(authId : String)  {
        var user = repository.findUserByAuthId(authId)
        mUser.postValue(user!!)
    }
    suspend fun gertImageUser() {
        val userAuthId = FirebaseAuth.getInstance().currentUser?.uid
        val user = repository.findUserByAuthId(userAuthId!!)
        val image = repository.findImageByUserId(user?.id)
        val bitmapImage = BitmapFactory.decodeByteArray(image?.image, 0, image?.image!!.size)
        imagebitmap.postValue(bitmapImage)
    }
}