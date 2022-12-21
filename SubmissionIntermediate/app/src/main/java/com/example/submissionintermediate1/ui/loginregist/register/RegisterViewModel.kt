package com.example.submissionintermediate1.ui.loginregist.register

import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import com.example.submissionintermediate1.data.Repositories

@ExperimentalPagingApi
class RegisterViewModel constructor(private val repositories: Repositories): ViewModel() {


    suspend fun register(username: String, email: String, password: String) =
        repositories.register(username, email, password)
}