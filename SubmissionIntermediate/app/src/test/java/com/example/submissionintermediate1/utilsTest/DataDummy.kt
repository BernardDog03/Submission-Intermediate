package com.example.submissionintermediate1.utilsTest

import com.example.submissionintermediate1.data.database.StoryResponseItem
import com.example.submissionintermediate1.data.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

object DataDummy {

    fun generateDummyAllStories(): StoriesResponse {
        val item = mutableListOf<Stories>()
        val error = false
        val message = "Stories fetched successfully"
        for (i in 0..100) {
            val stories = Stories(
                id = "story-FvU4u0Vp2S3PMsFg",
                name = "Dimas",
                description = "Lorem Ipsum",
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1641623658595_dummy-pic.png",
                createdAt = "2022-01-08T06:34:18.598Z",
                lat = -10.212,
                lon = -16.002
            )
            item.add(stories)
        }
        return StoriesResponse(error, message, item)
    }

    fun generateDummyDetailStories(): List<StoryResponseItem> {
        val item = arrayListOf<StoryResponseItem>()
        for (i in 0..10) {
            val stories = StoryResponseItem(
                id = "story-FvU4u0Vp2S3PMsFg",
                name = "Dimas",
                description = "Lorem Ipsum",
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1641623658595_dummy-pic.png",
                createdAt = "2022-01-08T06:34:18.598Z",
                lat = -10.212,
                lon = -16.002
            )
            item.add(stories)
        }
        return item
    }

    fun generateDummyLogin(): LoginResponse {
        val login = ResultUser(
            userId = "user-yj5pc_LARC_AgK61",
            name = "Arif Faizin",
            token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLXlqNXBjX0xBUkNfQWdLNjEiLCJpYXQiOjE2NDE3OTk5NDl9.flEMaQ7zsdYkxuyGbiXjEDXO8kuDTcI__3UjCwt6R_I"
        )
        return LoginResponse(
            error = false,
            message = "success",
            result = login
        )
    }

    fun generateDummyRegister(): RegisterResponse{
        return RegisterResponse(
            error = false,
            message = "success"
        )
    }

    fun generateDummyToken(): String {
        return "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLXlqNXBjX0xBUkNfQWdLNjEiLCJpYXQiOjE2NDE3OTk5NDl9.flEMaQ7zsdYkxuyGbiXjEDXO8kuDTcI__3UjCwt6R_I"
    }

    fun generateDummyMultipart(): MultipartBody.Part{
        val dummy = "Dummy Text"
        return MultipartBody.Part.create(dummy.toRequestBody())
    }

    fun generateDummyRequest(): RequestBody {
        val dummy = "Dummy Text"
        return dummy.toRequestBody()
    }

    fun generateDummyUpload(): AddNewStoriesResponse{
        return AddNewStoriesResponse(
            error = false,
            message = "success"
        )
    }
}