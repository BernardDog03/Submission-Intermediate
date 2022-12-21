package com.example.submissionintermediate1.ui.loginregist.login

import androidx.paging.ExperimentalPagingApi
import com.example.submissionintermediate1.data.Repositories
import com.example.submissionintermediate1.data.response.LoginResponse
import com.example.submissionintermediate1.utilsTest.DataDummy
import com.example.submissionintermediate1.utilsTest.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@ExperimentalPagingApi
@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {

    @get:Rule
    var coroutineTest = MainDispatcherRule()

    @Mock
    private lateinit var repo: Repositories
    private lateinit var  loginViewModel: LoginViewModel
    private val loginResponse = DataDummy.generateDummyLogin()
    private val dummyEmail = "rafifsuja12@gmail.com"
    private val dummyPassword = "password"
    private val dummyToken = "Token"

    @Before
    fun setup(){
        loginViewModel = LoginViewModel(repo)
    }

    @Test
    fun `test login successfully with dummy email and dummy password` (): Unit = runTest {
        val expectedResult = flow {
            emit(Result.success(loginResponse))
        }

        `when`(repo.login(dummyEmail,dummyPassword)).thenReturn(expectedResult)

        loginViewModel.login(dummyEmail,dummyPassword).collect{ result ->
            Assert.assertTrue(result.isSuccess)
            Assert.assertFalse(result.isFailure)

            result.onSuccess {
                Assert.assertNotNull(it)
                Assert.assertSame(loginResponse, it)
            }

        }
        verify(repo).login(dummyEmail,dummyPassword)
    }

    @Test
    fun `test login failed with dummy email dan dummy password` (): Unit = runTest {
        val expectedResult: Flow<Result<LoginResponse>> = flowOf(Result.failure(java.lang.Exception("failed")))
        `when`(repo.login(dummyEmail, dummyPassword)).thenReturn(expectedResult)

        loginViewModel.login(dummyEmail, dummyPassword).collect{ result ->
            Assert.assertFalse(result.isSuccess)
            Assert.assertTrue(result.isFailure)

            result.onFailure {
                Assert.assertNotNull(it)
            }

        }
        Mockito.verify(repo).login(dummyEmail,dummyPassword)
    }

    @Test
    fun `test save token with dummy token` (): Unit = runTest {
        loginViewModel.saveToken(dummyToken)
        verify(repo).saveToken(dummyToken)
    }

    @Test
    fun `test get token successfully and token not empty or null` () = runTest {
        val expectedResult = flowOf(dummyToken)
        `when`(repo.getToken()).thenReturn(expectedResult)

        loginViewModel.getToken().collect{
            Assert.assertNotNull(it)
            Assert.assertEquals(dummyToken, it)
        }
        verify(repo).getToken()
    }
}