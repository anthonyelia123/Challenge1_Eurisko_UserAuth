package com.example.eurisko_challenge.MVVM

import android.content.ContentValues
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.eurisko_challenge.Objects.User

class SignupModelView: ViewModel() {

    private val _email = MutableLiveData<String>()
    private val _pass = MutableLiveData<String>()
    private val _pass2 = MutableLiveData<String>()
    private val _firstName = MutableLiveData<String>()
    private val _lastName = MutableLiveData<String>()

    fun getEmail(): LiveData<String>{
        return _email
    }
    fun getFirstName(): LiveData<String> {
        return _firstName
    }
    fun getPass(): LiveData<String> {
        return _pass
    }
    fun getPass2(): LiveData<String> {
        return _pass2
    }
    fun getLastName(): LiveData<String> {
        return _lastName
    }

    fun setEmail(email: String){
        _email.value = email
    }
    fun setFirstName(firstName: String) {
        _firstName.value = firstName
    }
    fun setPass(pass: String) {
        _pass.value = pass
    }
    fun setLastName(lastName: String) {
        _lastName.value = lastName
    }
    fun setPass2(pass2: String) {
        _pass2.value = pass2
    }

    fun validEmail(email: String) : String? {
        if(email.isEmpty()){
            return "Field required"
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            return "Invalid email address"
        }
        return null
    }
    fun validPass(pass1: String) : String? {
        if(pass1.isEmpty()) {
            return "Field required"
        }
        if(pass1.length < 6) {
            return "More than 6 characters required"
        }
        return null
    }
    fun validPass2(pass1: String, pass2: String) : String?{
        if(pass2.isEmpty()) {
            return "Field required"
        }
        if(pass2.length < 6) {
            return "More than 6 characters required"
        }
        if(!pass1.equals(pass2)){
            return "Password doesn't match"
        }
        return null
    }


    fun validFirstName(firstName: String) : String? {
        if(firstName.isEmpty()){
            return "Field required"
        }
        return null
    }

    fun validLastName(lastName: String) : String? {
        if(lastName.isEmpty()){
            return "Field required"
        }
        return null
    }
}