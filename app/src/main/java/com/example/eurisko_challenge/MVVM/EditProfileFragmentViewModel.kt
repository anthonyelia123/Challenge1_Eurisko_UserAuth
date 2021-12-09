package com.example.eurisko_challenge.MVVM

import android.app.Application
import android.content.ContentValues
import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.example.eurisko_challenge.Objects.User
import com.example.eurisko_challenge.Objects.UsersImage
import com.example.eurisko_challenge.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class EditProfileFragmentViewModel(application: Application): AndroidViewModel(application) {

    private val _firstName = MutableLiveData<String>()
    private val _lastName = MutableLiveData<String>()

    public val _saveImageStatus = MutableStateFlow<String>("")

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
    fun saveChanges(contentValues: ContentValues, selections: String, selectionArgs: Array<String?>) {

        getApplication<Application>().contentResolver.update(User.CONTENT_URI, contentValues, selections, selectionArgs)

    }
    fun saveImageToDatabase(imageUriString: Bitmap, userId: String) {
        viewModelScope.launch {
            val selection = UsersImage.Columns.ID + " = ?"
            val selectionArgs = arrayOf(userId)
            val cursor = getApplication<Application>().contentResolver.query(UsersImage.CONTENT_URI,null,selection,selectionArgs,null)
            if(cursor?.count == 0) {
                // insert image to database
                val byteArrayOutputStream = ByteArrayOutputStream()
                imageUriString.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream)
                val bytesImage = byteArrayOutputStream.toByteArray()
                val values = ContentValues().apply {
                    put(UsersImage.Columns.ID, userId)
                    put(UsersImage.Columns.Image, bytesImage)
                }

                getApplication<Application>().contentResolver.insert(UsersImage.CONTENT_URI, values)
                _saveImageStatus.emit("success")

            } else {
                // update image from database
                val byteArrayOutputStream = ByteArrayOutputStream()
                imageUriString.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream)
                val bytesImage = byteArrayOutputStream.toByteArray()
                val values = ContentValues().apply {
                    put(UsersImage.Columns.ID, userId)
                    put(UsersImage.Columns.Image, bytesImage)
                }
                getApplication<Application>().contentResolver.update(UsersImage.CONTENT_URI, values, selection, selectionArgs)

                _saveImageStatus.emit("updated")
            }
        }

    }

}