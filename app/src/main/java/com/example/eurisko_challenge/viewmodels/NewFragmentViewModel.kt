package com.example.eurisko_challenge.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.eurisko_challenge.models.MostPopularDataModel
import com.example.eurisko_challenge.repositories.RetrofitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewFragmentViewModel @Inject constructor(val repository: RetrofitRepository) : ViewModel() {
    private val _postMutableLiveData = MutableLiveData<MostPopularDataModel>()
    fun getPostLiveData(): LiveData<MostPopularDataModel> {
        return _postMutableLiveData
    }

    suspend fun getPosts() {
        val responseData = repository.getPost()
        _postMutableLiveData.postValue(responseData.body())
    }
}