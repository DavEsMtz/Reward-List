package com.example.data.repositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.Event
import app.cash.turbine.test
import com.example.data.datasources.RewardsRemoteDataSourceImpl
import com.example.data.executors.RewardsApiService
import com.example.data.executors.RewardsRemoteDataSource
import com.example.data.utils.suspendedTest
import com.example.domain.DataSourceResultState
import com.example.domain.models.GenericErrors
import com.example.domain.models.Reward
import com.example.domain.models.RewardsErrors
import com.example.domain.repositories.RewardsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.setMain
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever
import retrofit2.Response
import java.io.IOException

class RewardsRepositoryImplTest {

    @get:Rule
    val testInstantTaskExecutor = InstantTaskExecutorRule()

    @Mock
    private lateinit var apiService: RewardsApiService

    private val repository: RewardsRepository by lazy {
        RewardsRepositoryImpl(
            RewardsRemoteDataSourceImpl(apiService)
        )
    }

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @Test
    fun `Test get rewards success`() = suspendedTest {
        // given
        whenever(apiService.getRewards()).thenReturn(
            Response.success(
                listOf(reward5, reward4, reward3, reward1, reward7, reward6, reward2)
            )
        )

        val expected = DataSourceResultState.Success(
            listOf(reward5, reward4, reward3, reward1, reward7, reward6, reward2)
        )

        repository.getRewards().test {
            // when
            val result = cancelAndConsumeRemainingEvents()

            // then
            with(result) {
                assertTrue(isNotEmpty())
                assertTrue(contains(Event.Item(expected)))
            }

        }
    }

    @Test
    fun `Test get rewards empty`() = suspendedTest {
        // given
        whenever(apiService.getRewards()).thenReturn(Response.success(listOf()))

        val expected = DataSourceResultState.Error(RewardsErrors.EmptyRewardList)

        // when
        repository.getRewards().test {
            val result = cancelAndConsumeRemainingEvents()

            // then
            with(result) {
                assertFalse(isEmpty())
                assertTrue(contains(Event.Item(expected)))
            }
        }
    }

    @Test
    fun `Test get rewards response failure`() = suspendedTest {
        // given
        whenever(apiService.getRewards()).thenReturn(
            Response.error(403, "Unauthorized".toResponseBody())
        )

        val expected = DataSourceResultState.Error(GenericErrors.GenericError)

        // when
        repository.getRewards().test {
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