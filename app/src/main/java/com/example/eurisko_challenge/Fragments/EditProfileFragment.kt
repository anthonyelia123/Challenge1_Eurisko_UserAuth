package com.example.eurisko_challenge.Fragments

import android.content.ContentValues
import android.content.Context
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
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.eurisko_challenge.MVVM.EditProfileFragmentViewModel
import com.example.eurisko_challenge.Models.UserModel
import com.example.eurisko_challenge.Objects.User
import com.example.eurisko_challenge.R
import com.google.android.material.textfield.TextInputLayout
import java.lang.RuntimeException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_USER = "user"

/**
 * A simple [Fragment] subclass.
 * Use the [EditProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var userModel: UserModel? = null
    private var listener: MoreFragment.OnClickCallBack? = null
    private val editProfileFragmentViewModel: EditProfileFragmentViewModel by viewModels()
    private lateinit var firstNameEditText: TextInputLayout
    private lateinit var lastNameEditText: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userModel = arguments?.getParcelable<UserModel>(ARG_USER)
        editProfileFragmentViewModel.getFirstName().observe(this, Observer {
            firstNameEditText.editText?.setText(it)
        })
        editProfileFragmentViewModel.getLastName().observe(this, Observer {
            lastNameEditText.editText?.setText(it)
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_edit_profile, container, false)
        view.findViewById<TextView>(R.id.emailTextView).setText(userModel?.email)
        firstNameEditText = view.findViewById<TextInputLayout>(R.id.first_name)
        lastNameEditText = view.findViewById<TextInputLayout>(R.id.last_name)

        val btn = view.findViewById<Button>(R.id.saveChangesBtn)
        btn.setOnClickListener {
            //check names validations
            val result = editProfileFragmentViewModel.checkNamesValidation(firstNameEditText.editText?.text.toString(), lastNameEditText.editText?.text.toString())
            if(result == "200") {
                // update name changes
                Log.d("EditProfile", "Save Changes")
                val values = ContentValues().apply {
                    put(User.Columns.User_FirstName, firstNameEditText.editText?.text.toString())
                    put(User.Columns.User_LastName, lastNameEditText.editText?.text.toString())
                }
                val selection = User.Columns.ID + " = ?"
                val selectionArgs = arrayOf(userModel?.id)
                listener?.saveChanges(values, selection, selectionArgs)
            } else if(result == "bothNameMissed") {
                firstNameEditText.helperText = "please enter your first name"
                lastNameEditText.helperText = "please enter your last name"
            } else if(result == "lastNameMissed") {
                lastNameEditText.helperText = "please enter your last name"
            } else if(result == "firstNameMissed"){
                firstNameEditText.helperText = "please enter your first name"
            }

        }

        //open and get image selected from gallery
        val imageBtn = view.findViewById<Button>(R.id.addNewPicBtn)
        imageBtn.setOnClickListener {
            listener?.getImageFromGalery()
        }

        return view

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