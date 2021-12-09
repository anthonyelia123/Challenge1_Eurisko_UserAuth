package com.example.eurisko_challenge.Retrofit

import com.example.eurisko_challenge.Models.MostPopularDataModel
import com.example.eurisko_challenge.Models.Result
import retrofit2.Call
import retrofit2.http.GET

interface PostInterface {
    @GET("svc/mostpopular/v2/mostviewed/all-sections/7.json?api-key=F60RN2ldjyDwJ6368RWcxSnOF0tP20qA")
    fun getPost(): Call<MostPopularDataModel>
}