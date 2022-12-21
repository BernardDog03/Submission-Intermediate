package com.example.submissionintermediate1.ui.loginregist.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import com.example.submissionintermediate1.data.Repositories
import com.example.submissionintermediate1.data.response.LoginResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@ExperimentalPagingApi
class LoginViewModel constructor(
    private val repositories: Repositories
): ViewModel(){

    suspend fun login(email: String, password: String): Flow<Result<LoginResponse>> = repositories.login(email, password)

    fun saveToken(token: String){
        viewModelScope.launch {
            repositories.saveToken(token)
        }
    }

    fun  getToken(): Flow<String?> = repositories.getToken()
}