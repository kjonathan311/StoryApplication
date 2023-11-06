package com.example.storyapplication.data.di

import androidx.room.Room
import com.example.storyapplication.data.datastore.UserPreference
import com.example.storyapplication.data.local.StoryDatabase
import com.example.storyapplication.data.repositories.LoginRepository
import com.example.storyapplication.data.repositories.MapRepository
import com.example.storyapplication.data.repositories.RegisterUserRepository
import com.example.storyapplication.data.repositories.StoryRepository
import com.example.storyapplication.data.repositories.UserRepository
import com.example.storyapplication.ui.main.MainViewModel
import com.example.storyapplication.ui.map.MapViewModel
import com.example.storyapplication.ui.story.StoryViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val userModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            StoryDatabase::class.java,
            "story_database"
        ).fallbackToDestructiveMigration().build()
    }

    single { UserPreference(get()) }
    single { UserRepository(get()) }
    single { LoginRepository(get()) }
    single { StoryRepository(get(),get()) }
    single { MapRepository(get()) }
    single { RegisterUserRepository(get()) }
    viewModel { MainViewModel(get(),get(),get()) }
    viewModel{ StoryViewModel(get(),get()) }
    viewModel{MapViewModel(get())}
}