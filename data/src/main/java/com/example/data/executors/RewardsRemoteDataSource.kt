package com.example.data.executors

import com.example.domain.DataSourceResultState
import com.example.domain.models.Reward

interface RewardsRemoteDataSource {
    suspend fun getRewards(): DataSourceResultState<List<Reward>>
}