package com.example.eurisko_challenge.viewmodels

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginModelView: ViewModel() {
    private val _email = MutableLiveData<String>()
    private val _pass = MutableLiveData<String>()

    fun getEmail(): LiveData<String> {
        return _email
    }
    fun getPass(): LiveData<String> {
        return _pass
    }

    fun setEmail(firstName: String) {
        _email.value = firstName
    }
    fun setPass(lastName: String) {
        _pass.value = lastName
    }

    fun validEmail(email: String) : String? {
        if(email.isEmpty()){
            return "Required field"
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            return "Invalid Email Address"
        }
        return null
    }
    fun validPass(pass: String) : String? {
        if(pass.isEmpty()){
            return "Field Required"
        }
        return null
    }
}