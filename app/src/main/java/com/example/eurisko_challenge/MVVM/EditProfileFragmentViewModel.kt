package com.example.eurisko_challenge.MVVM

import android.app.Application
import android.content.ContentValues
import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.example.eurisko_challenge.R
import com.example.eurisko_challenge.RoomDatabase.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import javax.inject.Inject

@HiltViewModel
class EditProfileFragmentViewModel @Inject constructor(private val repository: Repository): ViewModel() {


    private val _firstName = MutableLiveData<String>()
    private val _lastName = MutableLiveData<String>()
    private val _userUpdated = MutableLiveData<Boolean>()
    private val _imageUpdated = MutableLiveData<Boolean>()

    val _saveImageStatus = MutableStateFlow<String>("")

    fun isUserUpdated() : LiveData<Boolean>{
        return _userUpdated
    }

    fun getFirstName(): LiveData<String>{
        return _firstName
    }
    fun getLastName(): LiveData<String>{
        return _lastName
    }

    fun setFirstName(firstName: String) {
        _firstName.value = firstName
    }
    fun setLastName(lastName: String) {
        _lastName.value = lastName
    }

    fun checkNamesValidation(firstName:String, lastName:String): String{
        if(firstName.isNotEmpty() && lastName.isNotEmpty()){
            return "200"
        }
        if(firstName.isEmpty() && lastName.isEmpty()){
            return "bothNameMissed"
        }
        if(firstName.isEmpty()){
            return "firstNameMissed"
        }
        return "lastNameMissed"
    }
    // update user names in database
    suspend fun saveChanges(firstName: String, lastNameString: String) {
        var userAuthId = FirebaseAuth.getInstance().currentUser?.uid
        var userToUpdate = repository.findUserByAuthId(userAuthId!!)
        userToUpdate?.firstname = firstName
        userToUpdate?.lastname = lastNameString
        userToUpdate?.let{
            repository.update(it)
        }
        _userUpdated.postValue(true)
    }
    suspend fun saveImageToDatabase(imageUriString: Bitmap) {

        var userAuthId = FirebaseAuth.getInstance().currentUser?.uid
        var user = repository.findUserByAuthId(userAuthId!!)
        var image = repository.findImageByUserId(user?.id)
        if(image == null) {
            // insert image to database
            val byteArrayOutputStream = ByteArrayOutputStream()
            imageUriString.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream)
            val bytesImage = byteArrayOutputStream.toByteArray()
            val userImage  = UsersImages().apply {
                this.image = bytesImage
                this.userId = user!!.id
            }
            repository.insert(userImage)
            _saveImageStatus.emit("success")
        } else {
            val byteArrayOutputStream = ByteArrayOutputStream()
            imageUriString.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream)
            val bytesImage = byteArrayOutputStream.toByteArray()
            image.image = bytesImage
            repository.update(image)
            _saveImageStatus.emit("updated")
        }

    }

}