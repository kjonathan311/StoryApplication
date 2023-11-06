package com.example.storyapplication.ui.main


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.storyapplication.data.datastore.UserModel
import com.example.storyapplication.data.remote.response.LoginResult
import com.example.storyapplication.data.repositories.LoginRepository
import com.example.storyapplication.data.repositories.RegisterUserRepository
import com.example.storyapplication.data.repositories.UserRepository
import kotlinx.coroutines.launch

class MainViewModel(
    private val userRepository: UserRepository
    ,private val registerUserRepository: RegisterUserRepository
    ,private val loginRepository: LoginRepository) : ViewModel() {

    private val errorMessageLiveData = MutableLiveData<String?>()
    val Message: LiveData<String?> get() = errorMessageLiveData

    private val errorLoginLiveData = MutableLiveData<Boolean?>()
    val Login: LiveData<Boolean?> get() = errorLoginLiveData

    private val loginResultLiveData = MutableLiveData<LoginResult>()
    val loginData:LiveData<LoginResult?> get() = loginResultLiveData

    val loading = MutableLiveData<Boolean>().apply { value = false }

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            userRepository.saveSession(user)
        }
    }

    fun getSession(): LiveData<UserModel> {
        return userRepository.getSession().asLiveData()
    }

    fun login(name:String,email: String){
        viewModelScope.launch {
            loading.value = true
            errorLoginLiveData.value=null
            loginResultLiveData.value=loginRepository.login(name,email)
            errorLoginLiveData.value = loginResultLiveData.value!=null
            loading.value = false
        }
    }

    fun register(name:String, email:String, password:String){
        errorMessageLiveData.value = null
        viewModelScope.launch {
             loading.value = true
             val message = registerUserRepository.register(name, email, password)
             errorMessageLiveData.value = message
             loading.value = false
         }
    }

}