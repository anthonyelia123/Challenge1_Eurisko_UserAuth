package com.example.eurisko_challenge.FirebaseAuth

import android.app.Activity
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

class FirebaseUserAuth(private val listener: OnUserAuthenticate) {

    interface OnUserAuthenticate{
        fun onLogin(result: String)
        fun onSignup(result: String)
        fun onLogout(result: String)
        fun onChangePass(result: String)
    }
    private val mAuth = FirebaseAuth.getInstance()

    fun login(email: String, pass: String, activity: Activity){
        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
            if(task.isSuccessful){
                listener.onLogin("200")
            } else {
                listener.onLogin("500")
            }
        }
    }
    fun signup(email: String, pass: String){
        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
            if(task.isSuccessful){
                listener.onSignup("200")
            } else {
                listener.onSignup("500")
            }
        }
    }
    fun getUserId(): String{
        return mAuth.currentUser?.uid.toString()
    }
    fun getUserEmail():String{
        return mAuth.currentUser?.email.toString()
    }
    fun logout(){
        mAuth.signOut()
        listener.onLogout("200")
    }
    fun changePass(email: String, currentPass: String, newPass: String){
        val user = FirebaseAuth.getInstance().currentUser
        val credential = EmailAuthProvider.getCredential(email, currentPass)
        user?.reauthenticate(credential)?.addOnCompleteListener {
            if(it.isSuccessful){
                Log.d("NEw", "succ")
                user.updatePassword(newPass).addOnCompleteListener {
                    if(it.isSuccessful){
                        listener.onChangePass("200")
                    } else {
                        listener.onChangePass("500")
                    }
                }
            }else {
                listener.onChangePass("404")
            }

        }
    }
    fun isUserLoggedIn(): Boolean{
        if(mAuth.currentUser != null){
            return true
        }
        return false
    }
}