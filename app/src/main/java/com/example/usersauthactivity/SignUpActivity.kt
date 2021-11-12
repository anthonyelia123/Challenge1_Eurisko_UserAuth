package com.example.usersauthactivity

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
import com.example.usersauthactivity.databinding.ActivityMainBinding
import com.example.usersauthactivity.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.sql.DatabaseMetaData

private const val TAG = "SignUpActivity"
private const val EMAIL = "email"
private const val PASS = "password"
private const val PASS_2 = "password2"
private const val FIRST_NAME= "firstName"
private const val LAST_NAME = "lastName"
class SignUpActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var binding : ActivitySignUpBinding
    private lateinit var database : SQLiteDatabase
    private lateinit var progressDialog : ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        emailFocusListener()
        pass2FocusListener()
        firstNameFocusListener()
        lastNameFocusListener()
        passFocusListener()
        binding.signupBtn.setOnClickListener(this)
        binding.loginLink.setOnClickListener(this)
        mAuth = FirebaseAuth.getInstance()
        database = baseContext.openOrCreateDatabase("userInfo.db", Context.MODE_PRIVATE, null)
        //progress bar
        progressDialog = ProgressDialog(this)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        binding.signupEmail.editText?.setText(savedInstanceState.getString(EMAIL))
        binding.pass.editText?.setText(savedInstanceState.getString(PASS))
        binding.confirmPass.editText?.setText(savedInstanceState.getString(PASS_2))
        binding.firstName.editText?.setText(savedInstanceState.getString(FIRST_NAME))
        binding.lastName.editText?.setText(savedInstanceState.getString(LAST_NAME))
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(EMAIL, binding.signupEmail.editText?.text.toString())
        outState.putString(PASS, binding.pass.editText?.text.toString())
        outState.putString(PASS_2, binding.confirmPass.editText?.text.toString())
        outState.putString(FIRST_NAME, binding.firstName.editText?.text.toString())
        outState.putString(LAST_NAME, binding.lastName.editText?.text.toString())
        super.onSaveInstanceState(outState)
    }
    override fun onClick(v: View) {
        when(v.id){
            R.id.login_link -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            R.id.signup_btn -> {
                checkFieldsValidity()
                val validEmail = binding.signupEmail.helperText == null
                val validPass = binding.pass.helperText == null
                val validPass2 = binding.confirmPass.helperText == null
                val validFirstName = binding.firstName.helperText == null
                val validLastName = binding.lastName.helperText == null

                if(validEmail && validPass && validPass2 && validFirstName && validLastName) {

                    progressDialog.show()
                    progressDialog.setContentView(R.layout.progress_dialog)
                    //signup to database
                    Log.d(TAG, "signUpActivity: signup dataBase")
                    val email = binding.signupEmail.editText?.text.toString()
                    val pass = binding.pass.editText?.text.toString()
                    val firstName = binding.firstName.editText?.text.toString()
                    val lastName = binding.lastName.editText?.text.toString()
                    mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener() { task ->
                        if(task.isSuccessful){
                            //save to sqlite database
                            saveUserInfo(firstName, lastName)
                            progressDialog.dismiss()
                        } else {
                            Toast.makeText(this, "Email already exists", Toast.LENGTH_LONG).show()
                            progressDialog.dismiss()
                        }
                    }
                }
            }
        }
    }

    private fun saveUserInfo(firstName:String, lastName:String){
        val sql = "CREATE TABLE IF NOT EXISTS users(_id TEXT PRIMARY KEY NOT NULL, firstName TEXT, lastName TEXT)"
        database.execSQL(sql)
        val userId = mAuth.currentUser?.uid.toString()
        val values = ContentValues().apply {
            put("_id", userId)
            put("firstName",firstName )
            put("lastName", lastName)
        }
        val result = database.insert("users",null, values)
        Log.d(TAG,result.toString())
        val intent = Intent(this, WelcomeActivity::class.java)
        startActivity(intent)
        finish()
    }
    private fun emailFocusListener(){
        binding.signupEmail.editText?.setOnFocusChangeListener { v, hasFocus ->
            if(!hasFocus) {
                binding.signupEmail.helperText = validEmail()
            }
        }
    }
    private fun passFocusListener(){
        binding.pass.editText?.setOnFocusChangeListener { v, hasFocus ->
            if(!hasFocus) {
                binding.pass.helperText = validPass()
            }
        }
    }
    private fun pass2FocusListener(){
        binding.confirmPass.editText?.setOnFocusChangeListener { v, hasFocus ->
            if(!hasFocus) {
                binding.confirmPass.helperText = validPass2()
            }
        }
    }
    private fun firstNameFocusListener(){
        binding.firstName.editText?.setOnFocusChangeListener { v, hasFocus ->
            if(!hasFocus) {
                binding.firstName.helperText = validFirstName()
            }
        }
    }
    private fun lastNameFocusListener(){
        binding.lastName.editText?.setOnFocusChangeListener { v, hasFocus ->
            if(!hasFocus) {
                binding.lastName.helperText = validLastName()
            }
        }
    }
    private fun validEmail() : String? {
        val email = binding.signupEmail.editText?.text.toString()
        if(email.isEmpty()){
            return "Field required"
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            return "Invalid email address"
        }
        return null
    }
    private fun validPass() : String? {
        val pass1 = binding.pass.editText?.text.toString()
        if(pass1.isEmpty()) {
            return "Field required"
        }
        if(pass1.length < 6) {
            return "More than 6 characters required"
        }
        return null
    }
    private fun validPass2() : String?{
        val pass1 = binding.pass.editText?.text.toString()
        val pass2 = binding.confirmPass.editText?.text.toString()
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


    private fun validFirstName() : String? {
        val firstName = binding.firstName.editText?.text.toString()
        if(firstName.isEmpty()){
            return "Field required"
        }
        return null
    }

    private fun validLastName() : String? {
        val lastName = binding.lastName.editText?.text.toString()
        if(lastName.isEmpty()){
            return "Field required"
        }
        return null
    }

    private fun checkFieldsValidity() {
        //check email validation
        binding.signupEmail.helperText = validEmail()
        //check pass validation
        binding.pass.helperText = validPass()
        //check pass2 validation
        binding.confirmPass.helperText = validPass2()
        //check first validation
        binding.firstName.helperText = validFirstName()
        //check lastname validation
        binding.lastName.helperText = validLastName()
    }
}