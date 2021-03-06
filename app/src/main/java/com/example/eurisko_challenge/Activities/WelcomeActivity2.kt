package com.example.eurisko_challenge.Activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder.createSource
import android.graphics.ImageDecoder.decodeBitmap
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.eurisko_challenge.FirebaseAuth.FirebaseUserAuth
import com.example.eurisko_challenge.Fragments.AboutUsFragment
import com.example.eurisko_challenge.Fragments.ChangePassFragment
import com.example.eurisko_challenge.Fragments.EditProfileFragment
import com.example.eurisko_challenge.Fragments.MoreFragment
import com.example.eurisko_challenge.Models.UserModel
import com.example.eurisko_challenge.Fragments.NewFragment
import com.example.eurisko_challenge.MVVM.EditProfileFragmentViewModel
import com.example.eurisko_challenge.Objects.User
import com.example.eurisko_challenge.Objects.UsersImage
import com.example.eurisko_challenge.R
import com.example.eurisko_challenge.databinding.ActivityWelcome2Binding

import java.io.ByteArrayOutputStream

private const val TAG = "MoreActivity"
private const val IMAGE_REQUEST_CODE = 200
class WelcomeActivity2 : AppCompatActivity(), MoreFragment.OnClickCallBack, FirebaseUserAuth.OnUserAuthenticate {

    private var userModel = UserModel()
    private lateinit var binding : ActivityWelcome2Binding
    private lateinit var progressDialog : ProgressDialog
    var newFragment: Fragment? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcome2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        //get user profile info from the database
        getUserInfoFromDatabase()

        //init bottom navigation
        val bottomNavView = binding.bottomNavView

        //init bottom navigation on selectedListener
        bottomNavView.setOnItemSelectedListener { item ->
            var fragment: Fragment? = null
            when (item.itemId) {
                R.id.moreFragment -> {
                    fragment = MoreFragment.newInstance(userModel)
                }
                R.id.newFragment -> {
                    fragment = NewFragment()
                }
            }
            val oldFrag = supportFragmentManager.findFragmentById(R.id.nav_fragment)
            supportFragmentManager.beginTransaction().remove(oldFrag!!).replace(R.id.nav_fragment, fragment!!).commit()
            true
        }

        //init progress dialog
        progressDialog = ProgressDialog(this)

        // init new fragment to More fragment for start
        newFragment = MoreFragment.newInstance(userModel)

        //if activity is created for the first time replace fragmentContainerView with newFragment
        //if activity is recreate (e.g. after rotation) do not replace fragmentContainerView with newFragment
        if(savedInstanceState == null){
            supportFragmentManager.beginTransaction()
                .add(R.id.nav_fragment, newFragment!!)
                .commit()
        }

        //permission to read write external storage
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE),
            PackageManager.PERMISSION_GRANTED)
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

    @SuppressLint("SetTextI18n")
    override fun getUserName(view: View) {
        val textView = view as TextView
        getUserInfoFromDatabase()
        textView.text = "${getString(R.string.hello)} ${userModel.firstName} ${userModel.lastName}"

    }

    override fun getImageFromDatabase(view: View) {
        Log.d(TAG, "getImage")
        val selection = UsersImage.Columns.ID + " = ?"
        val args = arrayOf(userModel.id)
        val cursor = contentResolver.query(UsersImage.CONTENT_URI, null, selection, args, null)
        cursor.use {
            if (it != null) {
                while(it.moveToNext()){
                    with(it){
                        val bytesImage = it.getBlob(1)
                        val bitmapImage = BitmapFactory.decodeByteArray(bytesImage, 0, bytesImage.size)
                        val v = view as ImageView
                        v.setImageBitmap(bitmapImage)
                    }
                }
            }
        }
    }

    //when edit profile btn is clicked in MoreFragment
    override fun onEditProfileClicked() {
        val fragment = EditProfileFragment.newInstance(userModel)
        val oldfrag = supportFragmentManager.findFragmentById(R.id.nav_fragment)
            if(oldfrag != null){
                supportFragmentManager.beginTransaction()
                    .addToBackStack(null)
                    .remove(oldfrag)
                    .replace(R.id.nav_fragment, fragment)
                    .commit()
            }

    }

    //when change pass btn is clicked in MoreFragment
    override fun onEditPassClicked() {
        val fragment = ChangePassFragment.newInstance(userModel)
        val oldFrag = supportFragmentManager.findFragmentById(R.id.nav_fragment)
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .remove(oldFrag!!)
            .replace(R.id.nav_fragment, fragment)
            .commit()
    }

    //when about us btn is clicked in MoreFragment
    override fun onAboutUsClicked() {
        val fragment = AboutUsFragment()
        val oldFrag = supportFragmentManager.findFragmentById(R.id.nav_fragment)
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .remove(oldFrag!!)
            .replace(R.id.nav_fragment, fragment)
            .commit()
    }





    override fun onLogin(result: String) {

    }

    override fun onSignup(result: String) {

    }

    override fun onLogout(result: String) {
        if(result == "200"){
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onChangePass(result: String) {
        TODO("Not yet implemented")
    }


    //get user info from database
    fun getUserInfoFromDatabase(){
        val firebaseAuth = FirebaseUserAuth(this)
        val userId = firebaseAuth.getUserId()
        val selection = User.Columns.ID + " = ?"
        val selectionArgs = arrayOf(userId)
        val cursor = contentResolver.query(User.CONTENT_URI,
            null,
            selection,
            selectionArgs,
            null)
        cursor.use {
            if (it != null) {
                while(it.moveToNext()) {
                    // Cycle through all records
                    with(it) {
                        val id = getString(0)
                        val firstName = getString(1)
                        val lastName = getString(2)
                        userModel.firstName = firstName
                        userModel.lastName = lastName
                        userModel.id = id
                        userModel.email = firebaseAuth.getUserEmail()
                    }
                }
            }
        }
    }
}