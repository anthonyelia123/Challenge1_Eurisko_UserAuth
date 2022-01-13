package com.example.eurisko_challenge.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eurisko_challenge.R
import com.example.eurisko_challenge.models.Result
import com.example.eurisko_challenge.recycleview.MostPopularViewAdapter
import com.example.eurisko_challenge.viewmodels.NewFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


/**
 * A simple [Fragment] subclass.
 * Use the [NewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
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
        GlobalScope.launch(Dispatchers.IO) {
            newFragmentViewModel.getPosts()
        }
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