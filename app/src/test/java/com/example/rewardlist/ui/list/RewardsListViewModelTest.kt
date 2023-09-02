package com.example.rewardlist.ui.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.example.data.mappers.toExpandableSection
import com.example.domain.DataSourceResultState
import com.example.domain.models.GenericErrors
import com.example.domain.models.GroupedRewards
import com.example.domain.models.Reward
import com.example.domain.models.RewardsErrors
import com.example.domain.usescases.GetRewardsUseCase
import com.example.rewardlist.utils.suspendedTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

class RewardsListViewModelTest {

    @get:Rule
    val testInstantTaskExecutor = InstantTaskExecutorRule()

    @Mock
    private lateinit var rewardsUseCase: GetRewardsUseCase

    private val viewModel: RewardsListViewModel by lazy {
        RewardsListViewModel(rewardsUseCase)
    }

    @Before
    @OptIn(ExperimentalCoroutinesApi::class)
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @Test
    fun `Test getRewards success`() = suspendedTest {
        // given
        whenever(rewardsUseCase.invoke()).thenReturn(
            flowOf(
                DataSourceResultState.Success(
                    listOf(
                        GroupedRewards(listId = 1, listOf(reward1, reward3, reward5)),
                        GroupedRewards(listId = 2, listOf(reward2, reward4))
                    )
                )
            )
        )

        val expected = RewardsListViewState.SuccessRewards(
            listOf(
                GroupedRewards(listId = 1, listOf(reward1, reward3, reward5)).toExpandableSection(),
                GroupedRewards(listId = 2, listOf(reward2, reward4)).toExpandableSection()
            )
        )

        viewModel.viewState.test {
            // when
            viewModel.populateRewards()

            assertEquals(RewardsListViewState.LoadingRewards, awaitItem())
            assertEquals(expected, awaitItem())
        }
    }

    @Test
    fun `Test getRewards empty`() = suspendedTest {
        // given
        whenever(rewardsUseCase.invoke()).thenReturn(
            flowOf(DataSourceResultState.Error(RewardsErrors.EmptyRewardList))
        )

        val expected = RewardsListViewState.EmptyListError

        viewModel.viewState.test {
            // when
            viewModel.populateRewards()

            assertEquals(RewardsListViewState.LoadingRewards, awaitItem())
            assertEquals(expected, awaitItem())
        }
    }

    @Test
    fun `Test getRewards error`() = suspendedTest {
        // given
        whenever(rewardsUseCase.invoke()).thenReturn(
            flowOf(DataSourceResultState.Error(GenericErrors.GenericError))
        )

        val expected = RewardsListViewState.Error

        viewModel.viewState.test {
            // when
            viewModel.populateRewards()

            assertEquals(RewardsListViewState.LoadingRewards, awaitItem())
            assertEquals(expected, awaitItem())
        }
    }

}

/** Test Data **/

val reward1 = Reward(id = 1, listId = 1, name = "Item 2")
val reward2 = Reward(id = 2, listId = 2, name = "Item 3")
val reward3 = Reward(id = 3, listId = 1, name = "Item 34")
val reward4 = Reward(id = 4, listId = 2, name = "Item 42")
val reward5 = Reward(id = 5, listId = 1, name = "Item 342")