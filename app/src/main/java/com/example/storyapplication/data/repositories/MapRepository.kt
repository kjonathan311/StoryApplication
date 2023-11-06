package com.example.storyapplication.data.repositories

import android.util.Log
import com.example.storyapplication.data.remote.response.ErrorResponse
import com.example.storyapplication.data.remote.response.ListStoryItem
import com.example.storyapplication.data.remote.retrofit.ApiService
import com.google.gson.Gson
import retrofit2.HttpException

class MapRepository(private val apiService: ApiService) {
    suspend fun getStoriesWithLocation():List<ListStoryItem?>?{
        try {
            val response = apiService.getStoriesWithLocation(1)
            return response.listStory
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            return null
        }
    }
}