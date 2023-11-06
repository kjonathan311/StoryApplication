package com.example.storyapplication.data.repositories

import com.example.storyapplication.data.remote.response.ErrorResponse
import com.example.storyapplication.data.remote.response.LoginResult
import com.example.storyapplication.data.remote.retrofit.ApiService
import com.google.gson.Gson
import retrofit2.HttpException

class LoginRepository(private val apiService: ApiService) {
    suspend fun login(email: String, password: String): LoginResult? {
        try {
            val response = apiService.login(email,password)
            return response.loginResult
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            return null
        }
    }
}