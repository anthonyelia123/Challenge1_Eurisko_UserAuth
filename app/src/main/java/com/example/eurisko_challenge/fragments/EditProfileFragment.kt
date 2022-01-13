package com.example.eurisko_challenge.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.eurisko_challenge.R
import com.example.eurisko_challenge.viewmodels.EditProfileFragmentViewModel
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_USER = "user"

/**
 * A simple [Fragment] subclass.
 * Use the [EditProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class EditProfileFragment : Fragment() {

    private val editProfileFragmentViewModel: EditProfileFragmentViewModel by viewModels()

    private lateinit var firstNameEditText: TextInputLayout
    private lateinit var lastNameEditText: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //observe firstname in MVVM
        editProfileFragmentViewModel.getFirstName().observe(this, Observer {
            firstNameEditText.editText?.setText(it)
        })
        //observe lastName in MVVM
        editProfileFragmentViewModel.getLastName().observe(this, Observer {
            lastNameEditText.editText?.setText(it)
        })
        editProfileFragmentViewModel.isUserUpdated().observe(this, Observer {
            firstNameEditText.editText?.setText("")
            lastNameEditText.editText?.setText("")
            Toast.makeText(activity, "Names updated successfully", Toast.LENGTH_SHORT).show()
        })

    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_edit_profile, container, false)
        view.findViewById<TextView>(R.id.emailTextView).text = FirebaseAuth.getInstance().currentUser?.email.toString()
        firstNameEditText = view.findViewById<TextInputLayout>(R.id.first_name)
        lastNameEditText = view.findViewById<TextInputLayout>(R.id.last_name)

        val btn = view.findViewById<Button>(R.id.saveChangesBtn)
        btn.setOnClickListener {
            //check names validations
            val result = editProfileFragmentViewModel.checkNamesValidation(firstNameEditText.editText?.text.toString(), lastNameEditText.editText?.text.toString())
            if(result == "200") {
                // update name changes

                updateChanges()



            } else if(result == "bothNameMissed") {
                firstNameEditText.helperText = getString(R.string.enterfirstname)
                lastNameEditText.helperText = getString(R.string.enterlastname)
            } else if(result == "lastNameMissed") {
                lastNameEditText.helperText = getString(R.string.enterlastname)
            } else if(result == "firstNameMissed"){
                firstNameEditText.helperText = getString(R.string.enterfirstname)
            }

        }

        //open and get image selected from gallery
        val imageBtn = view.findViewById<Button>(R.id.addNewPicBtn)
        imageBtn.setOnClickListener {
            //permission to read write external storage
            ActivityCompat.requestPermissions(
                activity!!,
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                PackageManager.PERMISSION_GRANTED
            )

            lifecycleScope.launch {
                launch { getImageFromGalery() }
            }

        }
        return view
    }

    fun updateChanges(){

        var firstName = firstNameEditText.editText?.text.toString()
        var lastName = lastNameEditText.editText?.text.toString()

        GlobalScope.launch(Dispatchers.IO) {
            editProfileFragmentViewModel.saveChanges(firstName, lastName)
        }
    }

    // get image from gallery
    @RequiresApi(Build.VERSION_CODES.P)
     fun getImageFromGalery(){
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        //startActivityForResult(intent, IMAGE_REQUEST_CODE) is duplicated use registerForActivityResult instead
        getResult.launch(intent)
    }

    // on selected image from gallery
    @RequiresApi(Build.VERSION_CODES.P)
    private val getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val imageFilePath = it.data?.data
            val imageDecoder =
                activity?.let { it1 -> ImageDecoder.createSource(it1.contentResolver, imageFilePath!!) }

            val imageToStore = imageDecoder?.let { it1 -> ImageDecoder.decodeBitmap(it1) }
            saveImageToDatabase(imageToStore!!)
        }
    }

    //Save image to dataBase
    @SuppressLint("Recycle")
    fun saveImageToDatabase(imageUriString: Bitmap)  {

        GlobalScope.launch(Dispatchers.IO) {
            editProfileFragmentViewModel.saveImageToDatabase(imageUriString)
        }

        lifecycleScope.launchWhenCreated {
            editProfileFragmentViewModel._saveImageStatus.collect {
                if(it == "success"){
                    Toast.makeText(activity, getString(R.string.imagesaved), Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(activity, getString(R.string.imageupdated), Toast.LENGTH_LONG).show()
                }
            }
        }

    }


    override fun onDetach() {
        super.onDetach()
        editProfileFragmentViewModel.setFirstName(firstNameEditText.editText?.text.toString())
        editProfileFragmentViewModel.setLastName(lastNameEditText.editText?.text.toString())
    }


}