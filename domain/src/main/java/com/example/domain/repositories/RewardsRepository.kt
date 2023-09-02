package com.example.domain.repositories

import com.example.domain.DataSourceResultState
import com.example.domain.models.Reward
import kotlinx.coroutines.flow.Flow

interface RewardsRepository {
    suspend fun getRewards(): Flow<DataSourceResultState<List<Reward>>>
}