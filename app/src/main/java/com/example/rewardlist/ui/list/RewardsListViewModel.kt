package com.example.rewardlist.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.mappers.toExpandableSection
import com.example.domain.DataSourceResultState
import com.example.domain.models.RewardsErrors
import com.example.domain.usescases.GetRewardsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RewardsListViewModel @Inject constructor(private val rewardsUseCase: GetRewardsUseCase) :
    ViewModel() {

    private val _viewState =
        MutableStateFlow<RewardsListViewState>(RewardsListViewState.LoadingRewards)
    val viewState get() = _viewState

    fun populateRewards() {
        getRewards()
    }

    private fun getRewards() {
        viewModelScope.launch {
            rewardsUseCase.invoke().collect { resultState ->
                when (resultState) {
                    is DataSourceResultState.Success -> {
                        _viewState.emit(RewardsListViewState.SuccessRewards(
                            resultState.data.map { groupedRewards -> groupedRewards.toExpandableSection() }
                        ))
                    }

                    is DataSourceResultState.Error -> handleError(resultState.error)
                }
            }
        }
    }

    private fun handleError(error: Throwable) {
        viewModelScope.launch {
            when (error) {
                is RewardsErrors.EmptyRewardList -> {
                    _viewState.emit(RewardsListViewState.EmptyListError)
                }

                else -> {
                    _viewState.emit(RewardsListViewState.Error)
                }
            }
        }
    }

}