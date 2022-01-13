package com.example.eurisko_challenge.viewmodels

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.eurisko_challenge.repositories.Repository
import com.example.eurisko_challenge.roomdatabase.Users
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
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
        image?.let {
            val bitmapImage = BitmapFactory.decodeByteArray(it.image, 0, it.image!!.size)
            imagebitmap.postValue(bitmapImage)
        }

    }
}