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
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.example.eurisko_challenge.FirebaseAuth.FirebaseUserAuth
import com.example.eurisko_challenge.MVVM.EditProfileFragmentViewModel
import com.example.eurisko_challenge.MVVM.WelcomeViewModel
import com.example.eurisko_challenge.Models.UserModel
import com.example.eurisko_challenge.R
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.RuntimeException
import java.util.Observer


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_USER = "user"
/**
 * A simple [Fragment] subclass.
 * Use the [MoreFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class MoreFragment : Fragment() {

    private var user: UserModel? = null
    private var listener: OnClickCallBack?= null
    private val welcomeViewModel: WelcomeViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        user = arguments?.getParcelable<UserModel>(ARG_USER)




    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_more, container, false)
        val editProfileBtn = view.findViewById<Button>(R.id.editProfileBtn)
        val changePassBtn = view.findViewById<Button>(R.id.changePassBtn)
        val aboutUsBtn = view.findViewById<Button>(R.id.aboutUsBtn)
        val imageView = view.findViewById<ImageView>(R.id.profileImage)
        val textView = view.findViewById<TextView>(R.id.userinfo)

        //get user image if exist from database
        //listener?.getImageFromDatabase(imageView)

        //get user names from database
        welcomeViewModel.getmUser().observe(viewLifecycleOwner, androidx.lifecycle.Observer {

            textView.setText("Hello ${it.firstname} ${it.lastname}")
        })
        welcomeViewModel.getimage().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            imageView.setImageBitmap(it)
        })
        //listener?.getUserName(textView)

        //call onEditPassClicked() method when change pass btn is clicked
        editProfileBtn.setOnClickListener {
            listener?.onEditProfileClicked()
        }
        //call onEditPassClicked() method when change pass btn is clicked
        changePassBtn.setOnClickListener {
            listener?.onEditPassClicked()
        }

        //call onAboutUsClicked() method when save changes btn is clicked
        aboutUsBtn.setOnClickListener {
            listener?.onAboutUsClicked()
        }
        getUserInfoFromDatabase()
        getUserImageFromDatabse()
        return view
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is OnClickCallBack){
            listener = context
        } else {
            throw RuntimeException("$context must implement OnClickCallBack")
        }

    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    //get user info from database
    fun getUserInfoFromDatabase(){
        val authUserId = FirebaseAuth.getInstance().currentUser?.uid
        GlobalScope.launch(Dispatchers.IO) {
            welcomeViewModel.getUser(authUserId!!)
        }
    }
    fun getUserImageFromDatabse(){
        GlobalScope.launch(Dispatchers.IO) {
            welcomeViewModel.gertImageUser()
        }
    }

    interface OnClickCallBack{
        fun getUserName(view: View)
        fun onEditProfileClicked()
        fun onEditPassClicked()
        fun onAboutUsClicked()
        fun getImageFromDatabase(view: View)
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param mUser Parameter 1.
         * @return A new instance of fragment MoreFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(mUser: UserModel) =
            MoreFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_USER, mUser)

                }
            }
    }
}