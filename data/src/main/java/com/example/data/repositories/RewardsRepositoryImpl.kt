package com.example.data.repositories

import com.example.data.executors.RewardsRemoteDataSource
import com.example.domain.DataSourceResultState
import com.example.domain.models.Reward
import com.example.domain.repositories.RewardsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RewardsRepositoryImpl(private val remoteDataSource: RewardsRemoteDataSource) :
    RewardsRepository {

    override suspend fun getRewards(): Flow<DataSourceResultState<List<Reward>>> =
        flow {
            emit(remoteDataSource.getRewards())
        }

}