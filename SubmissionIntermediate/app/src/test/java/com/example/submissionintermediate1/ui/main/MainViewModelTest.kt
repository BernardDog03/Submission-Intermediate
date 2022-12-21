package com.example.submissionintermediate1.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.submissionintermediate1.adapter.ListAdapter
import com.example.submissionintermediate1.data.Repositories
import com.example.submissionintermediate1.data.database.StoryResponseItem
import com.example.submissionintermediate1.data.response.AddNewStoriesResponse
import com.example.submissionintermediate1.utilsTest.DataDummy
import com.example.submissionintermediate1.utilsTest.MainDispatcherRule
import com.example.submissionintermediate1.utilsTest.PagingTest
import com.example.submissionintermediate1.utilsTest.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@ExperimentalPagingApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    @get:Rule
    var coroutineApi = MainDispatcherRule()

    @get:Rule
    var instantExecutor = InstantTaskExecutorRule()

    @Mock
    private lateinit var repo: Repositories
    private lateinit var mainViewModel: MainViewModel
    private val uploadResponse = DataDummy.generateDummyUpload()
    private val multiPartResponse = DataDummy.generateDummyMultipart()
    private val bodyRequestResponse = DataDummy.generateDummyRequest()
    private val dummyStory = DataDummy.generateDummyDetailStories()
    private val dummyToken = "Token"

    @Before
    fun setup() {
        mainViewModel = MainViewModel(repo)
    }

    @Test
    fun `test get token successfully and token not empty or null`() = runTest {
        val expectedResult = flowOf(dummyToken)
        Mockito.`when`(repo.getToken()).thenReturn(expectedResult)

        mainViewModel.getToken().collect {
            Assert.assertNotNull(it)
            Assert.assertEquals(dummyToken, it)
        }
        Mockito.verify(repo).getToken()
    }

    @Test
    fun `delete token to logout`(): Unit = runTest {
        mainViewModel.deleteToken()
        Mockito.verify(repo).deleteToken()
    }

    @Test
    fun `test get all stories successfully with token`() = runTest {
        val data: PagingData<StoryResponseItem> = PagingTest.snapshot(dummyStory)
        val expectedResult: Flow<PagingData<StoryResponseItem>> = flow {
            emit(data)
        }

        Mockito.`when`(repo.getAllStories(dummyToken)).thenReturn(expectedResult)
        val actualResult: PagingData<StoryResponseItem> =
            mainViewModel.getStory(dummyToken).getOrAwaitValue()
        val differ = AsyncPagingDataDiffer(
            diffCallback = ListAdapter.DIFF_CALLBACK,
            updateCallback = noopListCallback,
            mainDispatcher = coroutineApi.testDispatcher,
            workerDispatcher = coroutineApi.testDispatcher
        )
        differ.submitData(actualResult)
        advanceUntilIdle()
        Mockito.verify(repo).getAllStories(dummyToken)
        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyStory.size, differ.snapshot().size)
    }

    @Test
    fun `test upload file successfully with token multipart and bodyRequest`() = runTest {

        val expectedResult = flowOf(Result.success(uploadResponse))
        Mockito.`when`(
            repo.uploadStory(
                dummyToken,
                multiPartResponse,
                bodyRequestResponse,
                null,
                null
            )
        ).thenReturn(expectedResult)
        mainViewModel.uploadStory(dummyToken, multiPartResponse, bodyRequestResponse, null, null)
            .collect { result ->

                Assert.assertTrue(result.isSuccess)
                Assert.assertFalse(result.isFailure)

                result.onSuccess {
                    Assert.assertNotNull(it)
                    Assert.assertSame(uploadResponse, it)
                }
            }
        Mockito.verify(repo)
            .uploadStory(dummyToken, multiPartResponse, bodyRequestResponse, null, null)
    }

    @Test
    fun `test upload file result failed`(): Unit = runTest {
        val expectedResult: Flow<Result<AddNewStoriesResponse>> =
            flowOf(Result.failure(java.lang.Exception("failed")))
        Mockito.`when`(
            repo.uploadStory(
                dummyToken,
                multiPartResponse,
                bodyRequestResponse,
                null,
                null
            )
        )
            .thenReturn(expectedResult)
        mainViewModel.uploadStory(dummyToken, multiPartResponse, bodyRequestResponse, null, null)
            .collect { result ->

                Assert.assertFalse(result.isSuccess)
                Assert.assertTrue(result.isFailure)

                result.onFailure {
                    Assert.assertNotNull(it)
                }

            }
        Mockito.verify(repo)
            .uploadStory(dummyToken, multiPartResponse, bodyRequestResponse, null, null)
    }

    private val noopListCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {
        }

        override fun onRemoved(position: Int, count: Int) {
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {
        }

        override fun onChanged(position: Int, count: Int, payload: Any?) {
        }

    }
}