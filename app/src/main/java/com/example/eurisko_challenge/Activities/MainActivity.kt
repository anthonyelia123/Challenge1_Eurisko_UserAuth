package com.example.eurisko_challenge.Activities

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.example.eurisko_challenge.MVVM.LoginModelView
import com.example.eurisko_challenge.R
import com.example.eurisko_challenge.databinding.ActivityMainBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.eurisko_challenge.FirebaseAuth.FirebaseUserAuth


private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity(), View.OnClickListener, FirebaseUserAuth.OnUserAuthenticate {
    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseUserAuth
    private lateinit var progressDialog : ProgressDialog
    private val emailEditText by lazy { binding.loginEmail }
    private val passEditText by lazy { binding.loginPassword }
    private val loginViewModel: LoginModelView by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginViewModel.getEmail().observe(this, Observer {
            emailEditText.editText?.setText(it)
        })
        loginViewModel.getPass().observe(this, Observer {
            passEditText.editText?.setText(it)
        })

        binding.loginBtn.setOnClickListener(this)
        binding.signupLink.setOnClickListener(this)

        emailFocusListener()
        passFocusListener()

        progressDialog = ProgressDialog(this)
        firebaseAuth = FirebaseUserAuth(this)


    }


    override fun onStart() {
        super.onStart()
        if(firebaseAuth.isUserLoggedIn()){
            val intent = Intent(this, WelcomeActivity2::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        loginViewModel.setEmail(emailEditText.editText?.text.toString())
        loginViewModel.setPass(passEditText.editText?.text.toString())
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.signup_link-> {
                val intent = Intent(this,SignUpActivity::class.java)
                startActivity(intent)
            }
            R.id.login_btn -> {
                checkFieldsValidity()

                val validEmail = emailEditText.helperText == null
                val validPass = passEditText.helperText == null

                if(validEmail && validPass) {
                    val email = emailEditText.editText?.text.toString()
                    val pass = passEditText.editText?.text.toString()
                    progressDialog.show()
                    progressDialog.setContentView(R.layout.progress_dialog)
                    //check dataBase
                    firebaseAuth.login(email, pass, this)
                }
            }
        }
    }
    private fun emailFocusListener(){

        emailEditText.editText?.setOnFocusChangeListener { v, hasFocus ->
            if(!hasFocus) {
                emailEditText.helperText = loginViewModel.validEmail(emailEditText.editText?.text.toString())
            }
        }
    }
    private fun passFocusListener(){
        passEditText.editText?.setOnFocusChangeListener { v, hasFocus ->
            if(!hasFocus) {
               // binding.loginPassword.helperText = validPass()
                passEditText.helperText = loginViewModel.validPass(passEditText.editText?.text.toString())
            }
        }
    }

    private fun checkFieldsValidity(){
        //check email validation
        emailEditText.helperText = loginViewModel.validEmail(emailEditText.editText?.text.toString())
        //check pass validation
        passEditText.helperText = loginViewModel.validPass(passEditText.editText?.text.toString())
    }

    override fun onLogin(result: String) {
        if(result == "200"){
            Toast.makeText(this, getString(R.string.loginsuccesffuly), Toast.LENGTH_LONG).show()
            val intent = Intent(this, WelcomeActivity2::class.java)
            startActivity(intent)
            progressDialog.dismiss()
            finish()
        } else {
            Toast.makeText(this, getString(R.string.invalidUsernamOrPass), Toast.LENGTH_LONG).show()
            progressDialog.dismiss()
        }
    }

    override fun onSignup(result: String) {
        TODO("Not yet implemented")
    }

    override fun onLogout(result: String) {
        TODO("Not yet implemented")
    }

    override fun onChangePass(result: String) {
        TODO("Not yet implemented")
    }
}