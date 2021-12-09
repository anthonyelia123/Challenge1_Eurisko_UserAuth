package com.example.eurisko_challenge.MVVM

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.eurisko_challenge.Models.MostPopularDataModel
import com.example.eurisko_challenge.Models.Result
import com.example.eurisko_challenge.Retrofit.PostClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewFragmentViewModel: ViewModel() {
    private val _postMutableLiveData = MutableLiveData<MostPopularDataModel>()
    fun getPostLiveData(): LiveData<MostPopularDataModel> {
        return _postMutableLiveData
    }

    fun getPosts(){
        PostClient.getInstance().getPost().enqueue(object : Callback<MostPopularDataModel?> {
            override fun onResponse(call: Call<MostPopularDataModel?>, response: Response<MostPopularDataModel?>) {
                _postMutableLiveData.value = response.body()
            }

            override fun onFailure(call: Call<MostPopularDataModel?>, t: Throwable) {

            }
        })
    }
}