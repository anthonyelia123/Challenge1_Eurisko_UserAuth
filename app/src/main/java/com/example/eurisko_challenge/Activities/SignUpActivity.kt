package com.example.eurisko_challenge.Activities

import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import com.example.eurisko_challenge.MVVM.LoginModelView
import com.example.eurisko_challenge.MVVM.SignupModelView
import com.example.eurisko_challenge.R
import com.example.eurisko_challenge.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.eurisko_challenge.FirebaseAuth.FirebaseUserAuth
import com.example.eurisko_challenge.Objects.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.sql.DatabaseMetaData

private const val TAG = "SignUpActivity"
class SignUpActivity : AppCompatActivity(), View.OnClickListener, FirebaseUserAuth.OnUserAuthenticate {
    private lateinit var firebaseAuth: FirebaseUserAuth
    private lateinit var binding : ActivitySignUpBinding
    private lateinit var database : SQLiteDatabase
    private lateinit var progressDialog : ProgressDialog
    private val emailEditText by lazy { binding.signupEmail }
    private val passEditText by lazy { binding.pass }
    private val pass2EditText by lazy { binding.confirmPass }
    private val firstNameEditText by lazy { binding.firstName }
    private val lastNameEditText by lazy { binding.lastName }
    private val signupModelView: SignupModelView by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        signupModelView.getFirstName().observe(this, Observer {
            firstNameEditText.editText?.setText(it)
        })
        signupModelView.getLastName().observe(this, Observer {
            lastNameEditText.editText?.setText(it)
        })
        signupModelView.getEmail().observe(this, Observer {
            emailEditText.editText?.setText(it)
        })
        signupModelView.getPass().observe(this, Observer {
            passEditText.editText?.setText(it)
        })
        signupModelView.getPass2().observe(this, Observer {
            pass2EditText.editText?.setText(it)
        })

        emailFocusListener()
        pass2FocusListener()
        firstNameFocusListener()
        lastNameFocusListener()
        passFocusListener()

        binding.signupBtn.setOnClickListener(this)
        binding.loginLink.setOnClickListener(this)
        firebaseAuth = FirebaseUserAuth(this)
        database = baseContext.openOrCreateDatabase("userInfo.db", Context.MODE_PRIVATE, null)
        //progress bar
        progressDialog = ProgressDialog(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        signupModelView.setEmail(emailEditText.editText?.text.toString())
        signupModelView.setPass(passEditText.editText?.text.toString())
        signupModelView.setPass2(pass2EditText.editText?.text.toString())
        signupModelView.setFirstName(firstNameEditText.editText?.text.toString())
        signupModelView.setLastName(lastNameEditText.editText?.text.toString())
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.login_link -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            R.id.signup_btn -> {
                checkFieldsValidity()
                val validEmail = emailEditText.helperText == null
                val validPass = passEditText.helperText == null
                val validPass2 = pass2EditText.helperText == null
                val validFirstName = firstNameEditText.helperText == null
                val validLastName = lastNameEditText.helperText == null

                if(validEmail && validPass && validPass2 && validFirstName && validLastName) {

                    progressDialog.show()
                    progressDialog.setContentView(R.layout.progress_dialog)
                    //signup to database
                    Log.d(TAG, "signUpActivity: signup dataBase")
                    val email = emailEditText.editText?.text.toString()
                    val pass = passEditText.editText?.text.toString()
                    firebaseAuth.signup(email, pass)
                }
            }
        }
    }

    private fun saveUserInfo(){
        val firstName = firstNameEditText.editText?.text.toString()
        val lastName = lastNameEditText.editText?.text.toString()
        val userId = firebaseAuth.getUserId()
        val values = ContentValues().apply {
            put(User.Columns.ID, userId)
            put(User.Columns.User_FirstName,firstName )
            put(User.Columns.User_LastName, lastName)
        }

        contentResolver.insert(User.CONTENT_URI, values)
        val intent = Intent(this, WelcomeActivity2::class.java)
        startActivity(intent)
        finish()
    }
    private fun emailFocusListener(){
        binding.signupEmail.editText?.setOnFocusChangeListener { v, hasFocus ->
            if(!hasFocus) {
                emailEditText.helperText = signupModelView.validEmail(emailEditText.editText?.text.toString())
            }
        }
    }
    private fun passFocusListener(){
        binding.pass.editText?.setOnFocusChangeListener { v, hasFocus ->
            if(!hasFocus) {
                passEditText.helperText = signupModelView.validPass(passEditText.editText?.text.toString())
            }
        }
    }
    private fun pass2FocusListener(){
        binding.confirmPass.editText?.setOnFocusChangeListener { v, hasFocus ->
            if(!hasFocus) {
                pass2EditText.helperText = signupModelView.validPass2(passEditText.editText?.text.toString(), pass2EditText.editText?.text.toString())
            }
        }
    }
    private fun firstNameFocusListener(){
        binding.firstName.editText?.setOnFocusChangeListener { v, hasFocus ->
            if(!hasFocus) {
                firstNameEditText.helperText = signupModelView.validFirstName(firstNameEditText.editText?.text.toString())
            }
        }
    }
    private fun lastNameFocusListener(){
        binding.lastName.editText?.setOnFocusChangeListener { v, hasFocus ->
            if(!hasFocus) {
                lastNameEditText.helperText = signupModelView.validLastName(lastNameEditText.editText?.text.toString())
            }
        }
    }


    private fun checkFieldsValidity() {
        //check email validation
        emailEditText.helperText = signupModelView.validEmail(emailEditText.editText?.text.toString())
        //check pass validation
        passEditText.helperText = signupModelView.validPass(passEditText.editText?.text.toString())
        //check pass2 validation
        pass2EditText.helperText = signupModelView.validPass2(passEditText.editText?.text.toString(), pass2EditText.editText?.text.toString())
        //check first validation
        firstNameEditText.helperText = signupModelView.validFirstName(firstNameEditText.editText?.text.toString())
        //check lastname validation
        lastNameEditText.helperText = signupModelView.validLastName(lastNameEditText.editText?.text.toString())
    }

    override fun onLogin(result: String) {
        TODO("Not yet implemented")
    }

    override fun onSignup(result: String) {
        if(result == "200"){
            saveUserInfo()
            progressDialog.dismiss()
        } else {
            Toast.makeText(this, "Email already exists", Toast.LENGTH_LONG).show()
            progressDialog.dismiss()
        }
    }

    override fun onLogout(result: String) {
        TODO("Not yet implemented")
    }

    override fun onChangePass(result: String) {
        TODO("Not yet implemented")
    }
}