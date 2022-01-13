package com.example.eurisko_challenge.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.eurisko_challenge.R
import com.example.eurisko_challenge.models.UserModel
import com.example.eurisko_challenge.viewmodels.WelcomeViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


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
            Navigation.findNavController(view)
                .navigate(R.id.action_moreFragment_to_editProfileFragment4)
        }
        //call onEditPassClicked() method when change pass btn is clicked
        changePassBtn.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_moreFragment_to_changePassFragment3)
        }

        //call onAboutUsClicked() method when save changes btn is clicked
        aboutUsBtn.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_moreFragment_to_aboutUsFragment2)
        }
        getUserInfoFromDatabase()
        getUserImageFromDatabse()
        return view
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