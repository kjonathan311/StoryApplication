package com.example.storyapplication.ui.story

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.storyapplication.data.remote.response.ListStoryItem
import com.example.storyapplication.data.remote.retrofit.ApiService

class StoryPagingSource(private val apiService: ApiService): PagingSource<Int, ListStoryItem>() {
    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return try {
            val position = params.key ?: INITIAL_INDEX
            val responseData = apiService.getStories(position, params.loadSize)
            val storyList=responseData.listStory ?: emptyList()
            LoadResult.Page(
                data = storyList,
                prevKey = if (position == INITIAL_INDEX) null else position - 1,
                nextKey = if (storyList.isNullOrEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    private companion object{
        private val INITIAL_INDEX=1
    }
}