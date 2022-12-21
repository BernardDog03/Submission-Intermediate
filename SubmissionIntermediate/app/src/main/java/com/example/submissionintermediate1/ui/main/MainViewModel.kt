package com.example.submissionintermediate1.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.submissionintermediate1.data.Repositories
import com.example.submissionintermediate1.data.database.StoryResponseItem
import com.example.submissionintermediate1.data.response.AddNewStoriesResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

@ExperimentalPagingApi
class MainViewModel constructor(private val repositories: Repositories) : ViewModel() {

    fun getToken(): Flow<String?> = repositories.getToken()


    fun getStory(token: String): LiveData<PagingData<StoryResponseItem>> =
        repositories.getAllStories(token).cachedIn(viewModelScope).asLiveData()

    fun deleteToken(){
        viewModelScope.launch {
            repositories.deleteToken()
        }
    }

    suspend fun uploadStory(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody?,
        lon: RequestBody?
    ): Flow<Result<AddNewStoriesResponse>> = repositories.uploadStory(token, file, description,lat,lon)
}