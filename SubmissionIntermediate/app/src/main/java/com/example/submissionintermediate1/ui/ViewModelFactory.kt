package com.example.submissionintermediate1.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.ExperimentalPagingApi
import com.example.submissionintermediate1.data.Repositories
import com.example.submissionintermediate1.ui.loginregist.login.LoginViewModel
import com.example.submissionintermediate1.ui.loginregist.register.RegisterViewModel
import com.example.submissionintermediate1.ui.main.MainViewModel
import com.example.submissionintermediate1.ui.maps.MapsViewModel
import com.example.submissionintermediate1.utils.Injection

@ExperimentalPagingApi
class ViewModelFactory constructor(private val repositories: Repositories): ViewModelProvider.NewInstanceFactory(){

    @OptIn(ExperimentalPagingApi::class)
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(LoginViewModel::class.java)){
            return LoginViewModel(repositories) as T
        }
        else if (modelClass.isAssignableFrom(RegisterViewModel::class.java)){
            return RegisterViewModel(repositories) as T
        }
        else if (modelClass.isAssignableFrom(MainViewModel::class.java)){
            return MainViewModel(repositories) as T
        }
        else if (modelClass.isAssignableFrom(MapsViewModel::class.java)){
            return MapsViewModel(repositories) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")

    }

    companion object{
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory = instance?: synchronized(this){
            instance?: ViewModelFactory(Injection.provideRepository(context))
        }.also {
            instance = it
        }
    }
}