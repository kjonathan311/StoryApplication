package com.example.storyapplication.ui.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyapplication.data.datastore.UserModel
import com.example.storyapplication.data.remote.response.ListStoryItem
import com.example.storyapplication.data.repositories.StoryRepository
import com.example.storyapplication.data.repositories.UserRepository
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryViewModel(
    private val userRepository: UserRepository,
    private val storyRepository: StoryRepository
):ViewModel() {
    val loading = MutableLiveData<Boolean>().apply { value = true }

    val checkUpload = MutableLiveData<Boolean>().apply { value = false }

    var listStory:LiveData<PagingData<ListStoryItem>> = storyRepository.getListStories().cachedIn(viewModelScope)

    fun refreshList(){
        listStory=storyRepository.getListStories().cachedIn(viewModelScope)
    }

    fun logout(){
        viewModelScope.launch {
            loading.value=true
            userRepository.logout()
        }
    }
    fun setLoading(change:Boolean){
        loading.value=change
    }

    fun getSession(): LiveData<UserModel> {
        return userRepository.getSession().asLiveData()
    }



    suspend fun uploadStory(file: MultipartBody.Part, description: RequestBody){
        loading.value=true
        checkUpload.value=false
        try {
            val upload=storyRepository.uploadStory(file,description)
            if (upload){
                checkUpload.value=true
            }
        }catch (e:Exception){
            checkUpload.value=false
        }
        loading.value=false
    }

}