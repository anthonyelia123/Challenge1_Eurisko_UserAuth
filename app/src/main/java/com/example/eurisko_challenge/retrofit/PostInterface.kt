package com.example.eurisko_challenge.retrofit

import com.example.eurisko_challenge.models.MostPopularDataModel
import retrofit2.Response
import retrofit2.http.GET

interface PostInterface {
    @GET("svc/mostpopular/v2/mostviewed/all-sections/7.json?api-key=F60RN2ldjyDwJ6368RWcxSnOF0tP20qA")
    suspend fun getPost(): Response<MostPopularDataModel>
}