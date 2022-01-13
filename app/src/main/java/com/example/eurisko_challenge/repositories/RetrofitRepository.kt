package com.example.eurisko_challenge.repositories

import com.example.eurisko_challenge.models.MostPopularDataModel
import com.example.eurisko_challenge.retrofit.PostInterface
import retrofit2.Response
import javax.inject.Inject

class RetrofitRepository @Inject constructor(val postInterface: PostInterface) {
    suspend fun getPost(): Response<MostPopularDataModel> {
        return postInterface.getPost()
    }
}