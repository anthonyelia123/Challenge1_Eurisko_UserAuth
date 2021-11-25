package com.example.eurisko_challenge.Fragments

import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.eurisko_challenge.Models.UserModel
import com.example.eurisko_challenge.R
import java.lang.RuntimeException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_USER = "user"
private const val ARG_LISTENER = "listener"


/**
 * A simple [Fragment] subclass.
 * Use the [MoreFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MoreFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var user: UserModel? = null
    private var listener: OnClickCallBack?= null

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
        listener?.getImageFromDatabase(imageView)

        //get user names from database
        listener?.getUserName(textView)
        editProfileBtn.setOnClickListener {
            listener?.onEditProfileClicked()
        }
        //call ononEditPassClicked() method when change pass btn is clicked
        changePassBtn.setOnClickListener {
            listener?.onEditPassClicked()
        }

        //call onAboutUsClicked() method when save changes btn is clicked
        aboutUsBtn.setOnClickListener {
            listener?.onAboutUsClicked()
        }
        return view
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is OnClickCallBack){
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnClickCallBack")
        }

    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnClickCallBack{
        fun getUserName(view: View)
        fun onEditProfileClicked()
        fun onEditPassClicked()
        fun onAboutUsClicked()
        fun saveChanges(contentValues: ContentValues, selections: String, selectionArgs: Array<String?>)
        fun changePass(currentPass:String, newPass: String)
        fun getImageFromGalery()
        fun getImageFromDatabase(view: View)
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param user Parameter 1.
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