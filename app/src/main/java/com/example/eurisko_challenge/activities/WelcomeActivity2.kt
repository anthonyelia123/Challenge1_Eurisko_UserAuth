package com.example.eurisko_challenge.activities

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.eurisko_challenge.R
import com.example.eurisko_challenge.databinding.ActivityWelcome2Binding
import com.example.eurisko_challenge.firebaseauth.FirebaseUserAuth
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "MoreActivity"
private const val IMAGE_REQUEST_CODE = 200

@AndroidEntryPoint
class WelcomeActivity2 : AppCompatActivity(), FirebaseUserAuth.OnUserAuthenticate {

    private lateinit var binding: ActivityWelcome2Binding
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcome2Binding.inflate(layoutInflater)
        setContentView(binding.root)


        //init bottom navigation
        val bottomNavView = binding.bottomNavView
        val navController = findNavController(R.id.nav_fragment)
        /*val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_fragment) as NavHostFragment
        val navController = navHostFragment.navController*/

        bottomNavView.setupWithNavController(navController)
        //init progress dialog
        progressDialog = ProgressDialog(this)


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_logout -> {
                val firebaseAuth = FirebaseUserAuth(this)
                firebaseAuth.logout()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onLogout(result: String) {
        if(result == "200"){
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onChangePass(result: String) {

    }

    override fun onLogin(result: String) {

    }

    override fun onSignup(result: String) {

    }

}