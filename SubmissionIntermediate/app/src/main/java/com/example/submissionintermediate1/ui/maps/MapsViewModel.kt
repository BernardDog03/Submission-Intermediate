package com.example.submissionintermediate1.ui.maps

import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import com.example.submissionintermediate1.data.Repositories

@ExperimentalPagingApi
class MapsViewModel constructor(private val repository: Repositories) : ViewModel() {

    fun getAllStoriesWithLocation(token: String) = repository.getStoriesLocation(token)

}