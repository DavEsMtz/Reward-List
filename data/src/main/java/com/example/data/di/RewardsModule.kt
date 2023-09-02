package com.example.data.di

import com.example.data.datasources.RewardsRemoteDataSourceImpl
import com.example.data.executors.RewardsApiService
import com.example.data.executors.RewardsRemoteDataSource
import com.example.data.repositories.RewardsRepositoryImpl
import com.example.domain.repositories.RewardsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import retrofit2.Retrofit


@Module
@InstallIn(ViewModelComponent::class)
internal class RewardsModule {

    @Provides
    fun providesRewardsApiService(retrofit: Retrofit): RewardsApiService =
        retrofit.create(RewardsApiService::class.java)

    @Provides
    fun providesRewardsRemoteDataSource(apiService: RewardsApiService): RewardsRemoteDataSource =
        RewardsRemoteDataSourceImpl(apiService)

    @Provides
    fun providesRewardsRepository(remoteDataSource: RewardsRemoteDataSource): RewardsRepository =
        RewardsRepositoryImpl(remoteDataSource)
}