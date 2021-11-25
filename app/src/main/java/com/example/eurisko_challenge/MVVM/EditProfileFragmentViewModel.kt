package com.example.eurisko_challenge.MVVM

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class EditProfileFragmentViewModel: ViewModel() {

    private val _firstName = MutableLiveData<String>()
    private val _lastName = MutableLiveData<String>()

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

}