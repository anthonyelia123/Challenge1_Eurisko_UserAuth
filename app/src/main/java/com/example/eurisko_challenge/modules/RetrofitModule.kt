package com.example.eurisko_challenge.modules

import com.example.eurisko_challenge.retrofit.PostInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RetrofitModule {

    @Provides
    fun providePostInterface(retrofit: Retrofit): PostInterface {
        return retrofit.create(PostInterface::class.java)
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.nytimes.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}