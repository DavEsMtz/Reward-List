package com.example.data.executors

import com.example.domain.models.Reward
import retrofit2.Response
import retrofit2.http.GET

internal interface RewardsApiService {
    @GET("/hiring.json")
    suspend fun getRewards(): Response<List<Reward>>
}