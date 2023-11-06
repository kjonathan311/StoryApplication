package com.example.storyapplication.data.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.storyapplication.data.QuoteRemoteMediator
import com.example.storyapplication.data.local.StoryDatabase
import com.example.storyapplication.data.remote.response.ErrorResponse
import com.example.storyapplication.data.remote.response.ListStoryItem
import com.example.storyapplication.data.remote.retrofit.ApiService
import com.example.storyapplication.ui.story.StoryPagingSource
import com.google.gson.Gson
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException

class StoryRepository(
    private val storyDatabase: StoryDatabase,
    private val apiService: ApiService,
) {


    fun getListStories(): LiveData<PagingData<ListStoryItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 20
            ),
            remoteMediator = QuoteRemoteMediator(storyDatabase, apiService),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStory()
            }
        ).liveData
    }

    suspend fun uploadStory(file:MultipartBody.Part,description:RequestBody):Boolean{
        try {
            apiService.uploadStory(file,description)
            return true
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            Log.e("error upload",errorBody.toString())
            return false
        }
    }

}