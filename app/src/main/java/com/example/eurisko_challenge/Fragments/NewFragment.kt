package com.example.eurisko_challenge.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eurisko_challenge.MVVM.NewFragmentViewModel
import com.example.eurisko_challenge.Models.MostPopularDataModel
import com.example.eurisko_challenge.Models.Result
import com.example.eurisko_challenge.R
import com.example.eurisko_challenge.RecycleView.MostPopularViewAdapter
import com.example.eurisko_challenge.RecycleView.MostPopularViewHolder
import com.example.eurisko_challenge.Retrofit.PostClient
import com.example.eurisko_challenge.Retrofit.PostInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.sql.Array


/**
 * A simple [Fragment] subclass.
 * Use the [NewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewFragment : Fragment(), MostPopularViewAdapter.OnClickListener {

    private val newFragmentViewModel: NewFragmentViewModel by viewModels()
    private lateinit var mostPopularViewAdapter: MostPopularViewAdapter
    private var results: List<Result>? = null
    private val TAG = "NewFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        newFragmentViewModel.getPostLiveData().observe(this, Observer {
            mostPopularViewAdapter.loadNewData(it.results)
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_new, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.contents_recycle_view)
        newFragmentViewModel.getPosts()
        mostPopularViewAdapter = MostPopularViewAdapter(ArrayList(), this)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = mostPopularViewAdapter

        return view
    }


    override fun onItemClick(url: String) {
        val moreDetailsFragment = MoreDetailsFragment.newInstance(url)
        val oldFrag = activity?.supportFragmentManager?.findFragmentById(R.id.nav_fragment)
        if(oldFrag != null && moreDetailsFragment != null){
            activity?.supportFragmentManager?.beginTransaction()
                ?.addToBackStack(null)
                ?.remove(oldFrag)
                ?.replace(R.id.nav_fragment, moreDetailsFragment)
                ?.commit()
        }

    }

}