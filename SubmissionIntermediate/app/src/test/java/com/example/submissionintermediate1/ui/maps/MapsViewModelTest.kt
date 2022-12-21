package com.example.submissionintermediate1.ui.maps

import androidx.paging.ExperimentalPagingApi
import com.example.submissionintermediate1.data.Repositories
import com.example.submissionintermediate1.data.response.StoriesResponse
import com.example.submissionintermediate1.utilsTest.DataDummy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
@ExperimentalPagingApi
class MapsViewModelTest{

    @Mock
    private lateinit var repo: Repositories
    private lateinit var viewModel: MapsViewModel
    private val storiesMapsResponse = DataDummy.generateDummyAllStories()
    private val dummyToken = DataDummy.generateDummyToken()

    @Before
    fun setup(){
        viewModel = MapsViewModel(repo)
    }

    @Test
    fun `test get location base on story with token is successful` (): Unit = runTest {

        val expectedResult = flowOf(Result.success(storiesMapsResponse))
        Mockito.`when`(repo.getStoriesLocation(dummyToken)).thenReturn(expectedResult)
        viewModel.getAllStoriesWithLocation(dummyToken).collect{ result ->

            Assert.assertTrue(result.isSuccess)
            Assert.assertFalse(result.isFailure)

            result.onSuccess {
                Assert.assertNotNull(it)
                Assert.assertSame(storiesMapsResponse, it)
            }
        }
        Mockito.verify(repo).getStoriesLocation(dummyToken)
    }

    @Test
    fun `test get location base on story with token is failed` (): Unit = runTest {

        val expectedResult: Flow<Result<StoriesResponse>> = flowOf(Result.failure(Exception("failed")))
        Mockito.`when`(repo.getStoriesLocation(dummyToken)).thenReturn(
            expectedResult
        )
        viewModel.getAllStoriesWithLocation(dummyToken).collect{ result ->

            Assert.assertFalse(result.isSuccess)
            Assert.assertTrue(result.isFailure)

            result.onFailure {
                Assert.assertNotNull(it)
            }
        }
        Mockito.verify(repo).getStoriesLocation(dummyToken)
    }
}