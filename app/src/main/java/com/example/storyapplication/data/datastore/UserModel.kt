package com.example.storyapplication.data.datastore

data class UserModel (
    val userId: String,
    val token: String,
    val isLogin: Boolean = false
)