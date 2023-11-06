package com.example.storyapplication.data.repositories


import com.example.storyapplication.data.remote.response.ErrorResponse
import com.example.storyapplication.data.remote.retrofit.ApiService
import com.google.gson.Gson
import retrofit2.HttpException

class RegisterUserRepository(private val apiService: ApiService) {

    suspend fun register(name: String, email: String, password: String): String? {
        try {
            val response = apiService.register(name, email, password)
            return response.message
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            return errorBody.message
        }
    }
}