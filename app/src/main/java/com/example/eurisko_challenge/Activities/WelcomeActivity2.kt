package com.example.eurisko_challenge.Activities

import android.Manifest
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.eurisko_challenge.FirebaseAuth.FirebaseUserAuth
import com.example.eurisko_challenge.Fragments.AboutUsFragment
import com.example.eurisko_challenge.Fragments.ChangePassFragment
import com.example.eurisko_challenge.Fragments.EditProfileFragment
import com.example.eurisko_challenge.Fragments.MoreFragment
import com.example.eurisko_challenge.Models.UserModel
import com.example.eurisko_challenge.Fragments.NewFragment
import com.example.eurisko_challenge.Objects.User
import com.example.eurisko_challenge.Objects.UsersImage
import com.example.eurisko_challenge.R
import com.example.eurisko_challenge.databinding.ActivityWelcome2Binding

import java.io.ByteArrayOutputStream

private const val USER_INTENT = "user"
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
        bottomNavView.setOnNavigationItemSelectedListener { item ->
            var fragment: Fragment? = null
            when (item.itemId) {
                R.id.moreFragment -> {
                    fragment = MoreFragment.newInstance(userModel)
                }
                R.id.newFragment -> {
                    fragment = NewFragment()
                }
            }
            supportFragmentManager.beginTransaction().replace(R.id.nav_fragment, fragment!!).addToBackStack(null).commit()
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

    override fun getUserName(view: View) {
        val textView = view as TextView
        getUserInfoFromDatabase()
        textView.setText("Hello ${userModel.firstName} ${userModel.lastName}")

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
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.nav_fragment, fragment)
            .commit()
    }

    //when change pass btn is clicked in MoreFragment
    override fun onEditPassClicked() {
        val fragment = ChangePassFragment.newInstance(userModel)
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.nav_fragment, fragment)
            .commit()
    }

    //when about us btn is clicked in MoreFragment
    override fun onAboutUsClicked() {
        val fragment = AboutUsFragment()
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.nav_fragment, fragment)
            .commit()
    }

    // update user name in database
    override fun saveChanges(contentValues: ContentValues, selections: String, selectionArgs: Array<String?>) {
        progressDialog.show()
        progressDialog.setContentView(R.layout.progress_dialog)
        contentResolver.update(User.CONTENT_URI, contentValues, selections, selectionArgs)
        progressDialog.dismiss()
        Toast.makeText(this, "Names updated successfully", Toast.LENGTH_LONG).show()
    }

    //change user password
    override fun changePass(currentPass:String, newPass: String) {
        progressDialog.show()
        progressDialog.setContentView(R.layout.progress_dialog)
        val firebaseAuth = FirebaseUserAuth(this)
        firebaseAuth.changePass(userModel.email!!,currentPass, newPass)
    }

    // on change pass complete
    override fun onChangePass(result: String) {
        progressDialog.dismiss()
        if(result == "200"){
            Toast.makeText(this, "Password successfully changed", Toast.LENGTH_LONG).show()
        } else if(result == "404"){
            Toast.makeText(this, "Your current password is incorrect", Toast.LENGTH_LONG).show()
        } else if(result == "500") {
            Toast.makeText(this, "Password unsuccessfully changed", Toast.LENGTH_LONG).show()
        }
    }

    // get image from gallery
    override fun getImageFromGalery(){
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_REQUEST_CODE)
    }
    // on selected image from gallery
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK){
            val imageFilePath = data?.data
            val imageToStore = MediaStore.Images.Media.getBitmap(contentResolver, imageFilePath)
            saveImageToDatabase(imageToStore)
        }
    }


    override fun onLogin(result: String) {
        TODO("Not yet implemented")
    }

    override fun onSignup(result: String) {
        TODO("Not yet implemented")
    }

    override fun onLogout(result: String) {
        if(result == "200"){
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
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

    //Save image to dataBase
    fun saveImageToDatabase(imageUriString: Bitmap) {
        progressDialog.show()
        progressDialog.setContentView(R.layout.progress_dialog)
        val selection = UsersImage.Columns.ID + " = ?"
        val selectionArgs = arrayOf(userModel.id)
        var cursor = contentResolver.query(UsersImage.CONTENT_URI,null,selection,selectionArgs,null)
        if(cursor?.count == 0) {
            // insert image to database
            Log.d(TAG, "insert photo")
            val byteArrayOutputStream = ByteArrayOutputStream()
            imageUriString.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream)
            val bytesImage = byteArrayOutputStream.toByteArray()
            val values = ContentValues().apply {
                put(UsersImage.Columns.ID, userModel.id)
                put(UsersImage.Columns.Image, bytesImage)
            }

            contentResolver.insert(UsersImage.CONTENT_URI, values)
            progressDialog.dismiss()
            Toast.makeText(this, "Image saved successfully", Toast.LENGTH_LONG).show()

        } else {
            // update image from database
            Log.d(TAG, "update photo")
            val byteArrayOutputStream = ByteArrayOutputStream()
            imageUriString.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream)
            val bytesImage = byteArrayOutputStream.toByteArray()
            val values = ContentValues().apply {
                put(UsersImage.Columns.ID, userModel.id)
                put(UsersImage.Columns.Image, bytesImage)
            }
            contentResolver.update(UsersImage.CONTENT_URI, values, selection, selectionArgs)

            progressDialog.dismiss()
            Toast.makeText(this, "Image updated successfully", Toast.LENGTH_LONG).show()
        }
    }
}