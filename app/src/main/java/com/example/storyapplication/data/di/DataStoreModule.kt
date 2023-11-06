package com.example.storyapplication.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.storyapplication.data.datastore.UserPreference
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataStoreModule = module {
    single {
        provideDataStore(androidContext())
    }
    single{
        UserPreference(get())
    }
}

fun provideDataStore(context: Context): DataStore<Preferences> {
    return context.dataStore
}

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")
