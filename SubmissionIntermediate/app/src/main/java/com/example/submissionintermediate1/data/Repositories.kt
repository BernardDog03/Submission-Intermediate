package com.example.submissionintermediate1.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.submissionintermediate1.data.api.ApiService
import com.example.submissionintermediate1.data.database.StoryDatabase
import com.example.submissionintermediate1.data.database.StoryResponseItem
import com.example.submissionintermediate1.data.local.Token
import com.example.submissionintermediate1.data.response.AddNewStoriesResponse
import com.example.submissionintermediate1.data.response.LoginResponse
import com.example.submissionintermediate1.data.response.RegisterResponse
import com.example.submissionintermediate1.data.response.StoriesResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import okhttp3.RequestBody

@ExperimentalPagingApi
class Repositories constructor(
    private val apiService: ApiService,
    private val tokenPref: Token,
    private val database: StoryDatabase
){

    fun getAllStories(token: String): Flow<PagingData<StoryResponseItem>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            remoteMediator = RemoteMediator(
                database,
                apiService,
                bearerToken(token)
            ),
            pagingSourceFactory = {
                database.storiesDao().getStories()
            }
        ).flow
    }

    suspend fun uploadStory(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody? = null,
        lon: RequestBody?= null
    ): Flow<Result<AddNewStoriesResponse>> = flow {
        try {
            val tokenAuth = bearerToken(token)
            val response = apiService.addNewStories(tokenAuth, file, description,lat,lon)
            emit(Result.success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.failure(e))

        }
    }

    suspend fun register(
        name: String,
        email: String,
        password: String
    ): Flow<Result<RegisterResponse>> = flow {
        try {
            val response = apiService.userRegister(name, email, password)
            emit(Result.success(response))
        } catch (e: Exception) {

            e.printStackTrace()
            emit(Result.failure(e))

        }
    }.flowOn(Dispatchers.IO)

    suspend fun login(
        email: String,
        password: String
    ): Flow<Result<LoginResponse>> = flow {
        try {
            val response = apiService.userLogin(email, password)
            emit(Result.success(response))

        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.failure(e))
        }

    }.flowOn(Dispatchers.IO)

    suspend fun saveToken(token: String){
        tokenPref.saveToken(token)
    }

    fun getToken(): Flow<String?> = tokenPref.getToken()

    suspend fun deleteToken(){
        tokenPref.deleteAuthToken()
    }

    private fun bearerToken(token: String): String{
        return "Bearer $token"
    }

    fun getStoriesLocation(token: String): Flow<Result<StoriesResponse>> = flow{
        try {
            val bearerToken = bearerToken(token)
            val response = apiService.getAllStories(bearerToken, size = 30, location = 1)
            emit(Result.success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.failure(e))
        }
    }

    companion object{
        @Volatile
        private var instance: Repositories? = null
        fun getInstance(
            apiService: ApiService,
            tokenPref: Token,
            database: StoryDatabase
        ): Repositories = instance?: synchronized(this){
            instance?: Repositories(apiService, tokenPref, database)
        }.also {
            instance = it
        }
    }
}