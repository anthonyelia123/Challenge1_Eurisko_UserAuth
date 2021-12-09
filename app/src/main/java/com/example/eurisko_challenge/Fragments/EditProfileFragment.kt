package com.example.eurisko_challenge.Fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.eurisko_challenge.MVVM.EditProfileFragmentViewModel
import com.example.eurisko_challenge.Models.UserModel
import com.example.eurisko_challenge.Objects.User
import com.example.eurisko_challenge.R
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import java.lang.RuntimeException


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_USER = "user"

/**
 * A simple [Fragment] subclass.
 * Use the [EditProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditProfileFragment : Fragment() {

    private var userModel: UserModel? = null
    private var listener: MoreFragment.OnClickCallBack? = null

    private val editProfileFragmentViewModel: EditProfileFragmentViewModel by viewModels()

    private lateinit var firstNameEditText: TextInputLayout
    private lateinit var lastNameEditText: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //get the userModel passed
        userModel = arguments?.getParcelable<UserModel>(ARG_USER)

        //observe firstname in MVVM
        editProfileFragmentViewModel.getFirstName().observe(this, Observer {
            firstNameEditText.editText?.setText(it)
        })
        //observe lastName in MVVM
        editProfileFragmentViewModel.getLastName().observe(this, Observer {
            lastNameEditText.editText?.setText(it)
        })
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_edit_profile, container, false)
        view.findViewById<TextView>(R.id.emailTextView).text = userModel?.email
        firstNameEditText = view.findViewById<TextInputLayout>(R.id.first_name)
        lastNameEditText = view.findViewById<TextInputLayout>(R.id.last_name)

        val btn = view.findViewById<Button>(R.id.saveChangesBtn)
        btn.setOnClickListener {
            //check names validations
            val result = editProfileFragmentViewModel.checkNamesValidation(firstNameEditText.editText?.text.toString(), lastNameEditText.editText?.text.toString())
            if(result == "200") {
                // update name changes
                runBlocking(Dispatchers.IO) {
                    updateChanges()
                }
                Toast.makeText(activity, getString(R.string.nameUpdated), Toast.LENGTH_LONG).show()
                firstNameEditText.editText?.setText("")
                lastNameEditText.editText?.setText("")

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
            lifecycleScope.launch {
                launch { getImageFromGalery() }
            }

        }

        return view

    }
    suspend fun updateChanges(){

        val values = ContentValues().apply {
            put(User.Columns.User_FirstName, firstNameEditText.editText?.text.toString())
            put(User.Columns.User_LastName, lastNameEditText.editText?.text.toString())
        }
        val selection = User.Columns.ID + " = ?"
        val selectionArgs = arrayOf(userModel?.id)
        editProfileFragmentViewModel.saveChanges(values, selection, selectionArgs)


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
        //progressDialog.show()
        //progressDialog.setContentView(R.layout.progress_dialog)
        userModel?.id?.let { editProfileFragmentViewModel.saveImageToDatabase(imageUriString, it)}
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



    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is MoreFragment.OnClickCallBack){
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnClickCallBack")
        }

    }

    override fun onDetach() {
        super.onDetach()
        listener = null
        editProfileFragmentViewModel.setFirstName(firstNameEditText.editText?.text.toString())
        editProfileFragmentViewModel.setLastName(lastNameEditText.editText?.text.toString())
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param mUser Parameter 1.
         * @return A new instance of fragment EditProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(mUser: UserModel) =
            EditProfileFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_USER, mUser)
                }
            }
    }
}