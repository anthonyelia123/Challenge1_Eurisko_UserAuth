package com.example.eurisko_challenge.Retrofit

import com.example.eurisko_challenge.Models.MostPopularDataModel
import com.example.eurisko_challenge.Models.Result
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PostClient {
    private val BASE_URL = "https://api.nytimes.com/"
    private var postIntefcae: PostInterface? = null

    init {
        val retrofit = Retrofit.Builder()
                                .baseUrl(BASE_URL)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build()
        postIntefcae = retrofit.create(PostInterface::class.java)
    }

    companion object{
        private var instance : PostClient? = null

        fun getInstance(): PostClient{
            if(instance == null){
                instance = PostClient()
            }
            return instance!!
        }
    }

    fun getPost() : Call<MostPopularDataModel> {
        return postIntefcae!!.getPost()
    }

}