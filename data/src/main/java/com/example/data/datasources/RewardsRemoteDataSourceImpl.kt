package com.example.data.datasources

import com.example.data.executors.RewardsApiService
import com.example.data.executors.RewardsRemoteDataSource
import com.example.domain.DataSourceResultState
import com.example.domain.models.GenericErrors
import com.example.domain.models.Reward
import com.example.domain.models.RewardsErrors
import java.lang.Error

internal class RewardsRemoteDataSourceImpl(private val apiService: RewardsApiService) :
    RewardsRemoteDataSource {
    override suspend fun getRewards(): DataSourceResultState<List<Reward>> {
        return try {
            val response = apiService.getRewards()
            if (response.isSuccessful) {
                response.body()?.let { rewardsBody ->
                    if (rewardsBody.isNotEmpty())
                        DataSourceResultState.Success(rewardsBody)
                    else
                        DataSourceResultState.Error(RewardsErrors.EmptyRewardList)
                } ?: DataSourceResultState.Error(GenericErrors.GenericError)
            } else {
                DataSourceResultState.Error(GenericErrors.GenericError)
            }
        } catch (e: Exception) {
            DataSourceResultState.Error(GenericErrors.GenericError)
        }
    }
}