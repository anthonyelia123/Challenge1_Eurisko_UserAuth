package com.example.eurisko_challenge.Fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.eurisko_challenge.FirebaseAuth.FirebaseUserAuth
import com.example.eurisko_challenge.MVVM.ChangePassFragmentViewModel
import com.example.eurisko_challenge.Models.UserModel
import com.example.eurisko_challenge.R
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import java.lang.RuntimeException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_USER = "user"

/**
 * A simple [Fragment] subclass.
 * Use the [ChangePassFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChangePassFragment : Fragment(), FirebaseUserAuth.OnUserAuthenticate {
    // TODO: Rename and change types of parameters
    private var userModel: UserModel? = null
    private val changePassFragmentViewModel: ChangePassFragmentViewModel by viewModels()
    private lateinit var currentPassEditText: TextInputLayout
    private lateinit var newPassEditText: TextInputLayout
    private lateinit var confirmPassEditText: TextInputLayout
    private var listener: MoreFragment.OnClickCallBack? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userModel = arguments?.getParcelable(ARG_USER)
        changePassFragmentViewModel.getCurrentPass().observe(this, Observer {
            currentPassEditText.editText?.setText(it)
        })
        changePassFragmentViewModel.getNewPass().observe(this, Observer {
            newPassEditText.editText?.setText(it)
        })
        changePassFragmentViewModel.getConfirmPass().observe(this, Observer {
            confirmPassEditText.editText?.setText(it)
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_change_pass, container, false)
        currentPassEditText = view.findViewById<TextInputLayout>(R.id.current_pass)
        newPassEditText = view.findViewById<TextInputLayout>(R.id.new_pass)
        confirmPassEditText = view.findViewById<TextInputLayout>(R.id.confirm_pass)
        view.findViewById<Button>(R.id.saveChangesBtn).setOnClickListener {
            //check pass validation
            newPassEditText.helperText = changePassFragmentViewModel.validPass(newPassEditText.editText?.text.toString())
            //check pass2 validation
            confirmPassEditText.helperText = changePassFragmentViewModel.validPass2(newPassEditText.editText?.text.toString(), confirmPassEditText.editText?.text.toString())
            val validPass = newPassEditText.helperText == null
            val validPass2 = confirmPassEditText.helperText == null
            if(validPass && validPass2){
                //update pass in database
                listener?.changePass(currentPassEditText.editText?.text.toString(), newPassEditText.editText?.text.toString())
            }

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
        changePassFragmentViewModel.setCurrentPass(currentPassEditText.editText?.text.toString())
        changePassFragmentViewModel.setNewPass(newPassEditText.editText?.text.toString())
        changePassFragmentViewModel.setConfirmPass(confirmPassEditText.editText?.text.toString())

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param mUser Parameter 1.
         * @return A new instance of fragment ChangePassFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(mUser: UserModel) =
            ChangePassFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_USER, mUser)
                }
            }
    }

    override fun onLogin(result: String) {
        TODO("Not yet implemented")
    }

    override fun onSignup(result: String) {
        TODO("Not yet implemented")
    }

    override fun onLogout(result: String) {
        TODO("Not yet implemented")
    }

    override fun onChangePass(result: String) {

    }
}