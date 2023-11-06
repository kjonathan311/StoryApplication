package com.example.storyapplication.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyapplication.data.remote.response.ListStoryItem
import com.example.storyapplication.data.repositories.MapRepository

class MapViewModel(
    private val mapRepository: MapRepository
): ViewModel(){
    val loading = MutableLiveData<Boolean>().apply { value = true }

    private val _listStoryWithLocation = MutableLiveData<List<ListStoryItem?>>()
    val listStoryWithLocation: LiveData<List<ListStoryItem?>?> = _listStoryWithLocation

    suspend fun getStoriesWithLocation(){
        loading.value=true
        _listStoryWithLocation.value=mapRepository.getStoriesWithLocation()
        loading.value=false
    }
}