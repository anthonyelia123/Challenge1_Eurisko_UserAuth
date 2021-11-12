package com.example.usersauthactivity

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.usersauthactivity.databinding.ActivityMainBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.regex.Pattern
import java.util.zip.Inflater
private const val TAG = "MainActivity"
private const val EMAIL = "email"
private const val PASS = "password"
class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding : ActivityMainBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var progressDialog : ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginBtn.setOnClickListener(this)
        binding.signupLink.setOnClickListener(this)
        emailFocusListener()
        passFocusListener()

        progressDialog = ProgressDialog(this)
        mAuth = FirebaseAuth.getInstance()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        binding.loginEmail.editText?.setText(savedInstanceState.getString(EMAIL))
        binding.loginPassword.editText?.setText(savedInstanceState.getString(PASS))
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(EMAIL, binding.loginEmail.editText?.text.toString())
        outState.putString(PASS, binding.loginPassword.editText?.text.toString())
        super.onSaveInstanceState(outState)
    }

    override fun onStart() {
        super.onStart()
        if(mAuth.currentUser != null){
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.signup_link-> {
                val intent = Intent(this,SignUpActivity::class.java)
                startActivity(intent)
            }
            R.id.login_btn -> {
                checkFieldsValidity()

                val validEmail = binding.loginEmail.helperText == null
                val validPass = binding.loginPassword.helperText == null

                if(validEmail && validPass) {
                    val email = binding.loginEmail.editText?.text.toString()
                    val pass = binding.loginPassword.editText?.text.toString()
                    progressDialog.show()
                    progressDialog.setContentView(R.layout.progress_dialog)
                    //check dataBase
                    mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this, OnCompleteListener { task ->
                        if(task.isSuccessful) {
                            Toast.makeText(this, "Successfully Logged In", Toast.LENGTH_LONG).show()
                            val intent = Intent(this, WelcomeActivity::class.java)
                            startActivity(intent)
                            progressDialog.dismiss()
                            finish()
                        }else {
                            Toast.makeText(this, "invalid username or password", Toast.LENGTH_LONG).show()
                            progressDialog.dismiss()
                        }
                    })

                    Log.d(TAG, "check database")

                }
            }
        }
    }
    private fun emailFocusListener(){
        binding.loginEmail.editText?.setOnFocusChangeListener { v, hasFocus ->
            if(!hasFocus) {
                binding.loginEmail.helperText = validEmail()
            }
        }
    }
    private fun passFocusListener(){
        binding.loginPassword.editText?.setOnFocusChangeListener { v, hasFocus ->
            if(!hasFocus) {
                binding.loginPassword.helperText = validPass()
            }
        }
    }
    private fun validEmail() : String? {
        val email = binding.loginEmail.editText?.text.toString()
        if(email.isEmpty()){
            return "Required field"
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            return "Invalid Email Address"
        }
        return null
    }
    private fun validPass() : String? {
        val pass = binding.loginPassword.editText?.text.toString()
        if(pass.isEmpty()){
            return "Field Required"
        }
        return null
    }
    private fun checkFieldsValidity(){
        //check email validation
        binding.loginEmail.helperText = validEmail()
        //check pass validation
        binding.loginPassword.helperText = validPass()
    }




}