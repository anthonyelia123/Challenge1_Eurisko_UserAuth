package com.example.eurisko_challenge.Fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.eurisko_challenge.R


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_URL = "url"

/**
 * A simple [Fragment] subclass.
 * Use the [MoreDetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MoreDetailsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var url: String? = null
    private val TAG = "MoreDetailsFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        url = arguments?.getString(ARG_URL)
        Log.d(TAG, "on create: $url")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_more_details, container, false)
        val webView = view.findViewById<WebView>(R.id.webViewMoreDetails)
        Log.d(TAG, "on createView: $url")
        val urlstr: String? = url
        Log.d(TAG, "im here")
        webView.webViewClient = WebViewClient()
        Toast.makeText(context, getString(R.string.pleaseWaitAMoment), Toast.LENGTH_LONG).show()
        webView.apply {
            urlstr?.let { loadUrl(it) }
            settings.javaScriptEnabled = true
            settings.safeBrowsingEnabled = true
        }

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param url Parameter 1.
         * @return A new instance of fragment MoreDetailsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(url: String) =
            MoreDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_URL, url)
                }
            }
    }
}