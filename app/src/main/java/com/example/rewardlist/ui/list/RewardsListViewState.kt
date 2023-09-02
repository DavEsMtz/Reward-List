package com.example.rewardlist.ui.list

import com.example.data.models.ExpandableSection
import com.example.domain.models.Reward

sealed class RewardsListViewState {
    object LoadingRewards : RewardsListViewState()
    data class SuccessRewards(val rewards: List<ExpandableSection<Reward>>) : RewardsListViewState()
    object Error : RewardsListViewState()
    object EmptyListError : RewardsListViewState()
}