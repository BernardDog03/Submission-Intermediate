package com.example.submissionintermediate1.utilsTest

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.submissionintermediate1.data.database.StoryResponseItem

class PagingTest: PagingSource<Int, LiveData<List<StoryResponseItem>>>() {

    companion object{
        fun snapshot(item: List<StoryResponseItem>): PagingData<StoryResponseItem>{
            return PagingData.from(item)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LiveData<List<StoryResponseItem>>>): Int? {
        return  0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<StoryResponseItem>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}