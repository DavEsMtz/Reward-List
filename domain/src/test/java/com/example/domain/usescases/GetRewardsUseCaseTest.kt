package com.example.domain.usescases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.Event
import app.cash.turbine.test
import com.example.domain.DataSourceResultState
import com.example.domain.models.GenericErrors
import com.example.domain.models.GroupedRewards
import com.example.domain.models.Reward
import com.example.domain.models.RewardsErrors
import com.example.domain.repositories.RewardsRepository
import com.example.domain.utils.suspendedTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

class GetRewardsUseCaseTest {

    @get:Rule
    val testInstantTaskExecutor = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: RewardsRepository

    private val rewardsUseCase: GetRewardsUseCase by lazy {
        GetRewardsUseCase(repository)
    }

    @Before
    @OptIn(ExperimentalCoroutinesApi::class)
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @Test
    fun `Test get rewards successfully`() = suspendedTest {
        // given
        whenever(repository.getRewards()).thenReturn(
            flowOf(
                DataSourceResultState.Success(
                    listOf(
                        reward2, reward1, reward5, reward3, reward4, reward6, reward7
                    )
                )
            )
        )

        val expected =
            DataSourceResultState.Success(
                listOf(
                    GroupedRewards(listId = 1, listOf(reward1, reward3, reward5)),
                    GroupedRewards(listId = 2, listOf(reward2, reward4))
                )
            )

        // when
        rewardsUseCase.invoke().test {
            val result = cancelAndConsumeRemainingEvents()

            // then
            with(result) {
                assertFalse(isEmpty())
                assertTrue(contains(Event.Item(expected)))
            }
        }
    }

    @Test
    fun `Test get rewards empty`() = suspendedTest {
        // given
        whenever(repository.getRewards()).thenReturn(
            flowOf(DataSourceResultState.Error(RewardsErrors.EmptyRewardList))
        )

        val expected = DataSourceResultState.Error(RewardsErrors.EmptyRewardList)

        // when
        rewardsUseCase.invoke().test {
            val result = cancelAndConsumeRemainingEvents()

            // then
            with(result) {
                assertFalse(isEmpty())
                assertTrue(contains(Event.Item(expected)))
            }
        }
    }

    @Test
    fun `Test get rewards error`() = suspendedTest {
        // given
        whenever(repository.getRewards()).thenReturn(
            flowOf(DataSourceResultState.Error(GenericErrors.GenericError))
        )

        val expected = DataSourceResultState.Error(GenericErrors.GenericError)

        // when
        rewardsUseCase.invoke().test {
            val result = cancelAndConsumeRemainingEvents()

            // then
            with(result) {
                assertFalse(isEmpty())
                assertTrue(contains(Event.Item(expected)))
            }
        }
    }
}

/** Test Data **/

val reward1 = Reward(id = 1, listId = 1, name = "Item 2")
val reward2 = Reward(id = 2, listId = 2, name = "Item 3")
val reward3 = Reward(id = 3, listId = 1, name = "Item 34")
val reward4 = Reward(id = 4, listId = 2, name = "Item 42")
val reward5 = Reward(id = 5, listId = 1, name = "Item 342")
val reward6 = Reward(id = 5, listId = 1, name = null)
val reward7 = Reward(id = 5, listId = 1, name = "")

