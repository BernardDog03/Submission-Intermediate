package com.example.submissionintermediate1.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.paging.ExperimentalPagingApi
import com.example.submissionintermediate1.data.Repositories
import com.example.submissionintermediate1.data.api.ApiConfig
import com.example.submissionintermediate1.data.database.StoryDatabase
import com.example.submissionintermediate1.data.local.Token

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("token")
@ExperimentalPagingApi
object Injection {

    fun provideRepository(context: Context): Repositories{
        val apiService = ApiConfig.getApiService()
        val preferences = Token.getInstance(context.dataStore)
        val database = StoryDatabase.getDatabase(context)

        return Repositories.getInstance(apiService, preferences, database)
    }

}