package com.example.usersauthactivity

import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.usersauthactivity.databinding.ActivitySignUpBinding
import com.example.usersauthactivity.databinding.ActivityWelcomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.lang.Exception

class WelcomeActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var binding : ActivityWelcomeBinding
    private lateinit var database : SQLiteDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mAuth = FirebaseAuth.getInstance()
        val userId = mAuth.currentUser?.uid.toString()
        binding.logout.setOnClickListener(this)

        database = baseContext.openOrCreateDatabase("userInfo.db", Context.MODE_PRIVATE, null)
        try{
            val sql = "SELECT * FROM users"
            val query = database.rawQuery(sql, null)
            query.use {
                while(it.moveToNext()){
                    with(it){
                        if(getString(0) == userId){
                            val firstName = getString(1)
                            val lastName = getString(2)

                            binding.userinfo.setText("Hello $firstName $lastName")

                        }

                    }
                }
            }
        }catch (e :Exception){
            Log.d("MainTest", e.message.toString())
            Toast.makeText(this, "Error occurs please try again", Toast.LENGTH_SHORT).show()
        }


    }

    override fun onClick(v: View) {
        if(v.id == R.id.logout){
            mAuth.signOut()
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}