package com.example.submissionintermediate1.ui.loginregist.register

import androidx.paging.ExperimentalPagingApi
import com.example.submissionintermediate1.data.Repositories
import com.example.submissionintermediate1.data.response.RegisterResponse
import com.example.submissionintermediate1.utilsTest.DataDummy
import com.example.submissionintermediate1.utilsTest.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalPagingApi
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class RegisterViewModelTest {
    @get:Rule
    var coroutineTest = MainDispatcherRule()

    @Mock
    private lateinit var repo: Repositories
    private lateinit var  regisViewModel: RegisterViewModel
    private val regisResponse = DataDummy.generateDummyRegister()
    private val dummyUsername = "Rafif Suja"
    private val dummyEmail = "rafifsuja12@gmail.com"
    private val dummyPassword = "password"

    @Before
    fun setup(){
        regisViewModel = RegisterViewModel(repo)
    }

    @Test
    fun `test register successfully with dummy email and dummy password` (): Unit = runTest {
        val expectedResult = flowOf(Result.success(regisResponse))
        Mockito.`when`(repo.register(dummyUsername, dummyEmail, dummyPassword)).thenReturn(expectedResult)
        regisViewModel.register(dummyUsername, dummyEmail, dummyPassword).collect{ result ->

            Assert.assertTrue(result.isSuccess)
            Assert.assertFalse(result.isFailure)

            result.onSuccess {
                Assert.assertNotNull(it)
                Assert.assertSame(regisResponse, it)
            }

        }
        Mockito.verify(repo).register(dummyUsername, dummyEmail,dummyPassword)
    }

    @Test
    fun `test register failed with dummy email and dummy password` (): Unit = runTest {

        val expectedResult: Flow<Result<RegisterResponse>> = flowOf(Result.failure(Exception("failed")))
        Mockito.`when`(repo.register(dummyUsername, dummyEmail, dummyPassword)).thenReturn(expectedResult)
        regisViewModel.register(dummyUsername, dummyEmail, dummyPassword).collect{ result ->

            Assert.assertFalse(result.isSuccess)
            Assert.assertTrue(result.isFailure)

            result.onFailure {
                Assert.assertNotNull(it)
            }

        }
        Mockito.verify(repo).register(dummyUsername, dummyEmail,dummyPassword)
    }
}