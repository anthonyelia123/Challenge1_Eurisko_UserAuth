package com.example.eurisko_challenge.Models

data class MostPopularDataModel(
    val copyright: String,
    val num_results: Int,
    val results: List<Result>,
    val status: String
)