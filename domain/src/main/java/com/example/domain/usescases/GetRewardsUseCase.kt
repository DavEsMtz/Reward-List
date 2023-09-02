package com.example.domain.usescases

import com.example.domain.DataSourceResultState
import com.example.domain.models.GenericErrors
import com.example.domain.models.GroupedRewards
import com.example.domain.models.RewardsErrors
import com.example.domain.repositories.RewardsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import javax.inject.Inject

class GetRewardsUseCase @Inject constructor(private val repository: RewardsRepository) {
    suspend operator fun invoke(): Flow<DataSourceResultState<List<GroupedRewards>>> {
        return repository.getRewards().transform { rewardsResult ->
            when (rewardsResult) {
                is DataSourceResultState.Error -> emit(rewardsResult)
                is DataSourceResultState.Success -> {
                    val sections =
                        rewardsResult.data.filter { reward ->
                            !reward.name.isNullOrEmpty()
                        }.groupBy { reward -> reward.listId }
                            .mapValues { group ->
                                group.value.sortedBy { reward ->
                                    reward.name?.split(" ")?.last()?.toInt()
                                }
                            }.map { group ->
                                // Map<listId: Int, orderedRewards: List<Rewards>>
                                GroupedRewards(listId = group.key, rewards = group.value)
                            }.sortedBy { section -> section.listId }

                    emit(DataSourceResultState.Success(sections))
                }
            }
        }
    }
}