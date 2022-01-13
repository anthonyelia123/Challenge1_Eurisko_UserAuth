package com.example.eurisko_challenge.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ChangePassFragmentViewModel: ViewModel() {

    private val _currentPass = MutableLiveData<String>()
    private val _newPass = MutableLiveData<String>()
    private val _confirm = MutableLiveData<String>()

    fun getCurrentPass(): LiveData<String> {
        return _currentPass
    }
    fun getNewPass(): LiveData<String> {
        return _newPass
    }
    fun getConfirmPass(): LiveData<String> {
        return _confirm
    }

    fun setCurrentPass(currentPass: String) {
        _currentPass.value = currentPass
    }
    fun setNewPass(newPass: String) {
        _newPass.value = newPass
    }
    fun setConfirmPass(oldPass: String) {
        _confirm.value = oldPass
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
}