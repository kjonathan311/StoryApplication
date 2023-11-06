package com.example.storyapplication.ui.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.example.storyapplication.R
import com.example.storyapplication.data.datastore.UserModel
import com.example.storyapplication.databinding.ActivityMainBinding
import com.example.storyapplication.ui.story.StoryActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    lateinit var binding:ActivityMainBinding
    private var login_page=true

    private val mainViewModel: MainViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        mainViewModel.loading.observe(this, Observer {
            showLoading(it)
        })
        binding.tvAuthChange.setOnClickListener {
            setView()
        }
        createSession()
    }

    private fun createSession(){
        binding.btnLogin.setOnClickListener {
            if (login_page){
                val email = binding.edLoginEmail.text.toString()
                val password = binding.edLoginPassword.text.toString()
                mainViewModel.login(email,password)
                mainViewModel.Login.observe(this, Observer {
                    if (it == false){
                        showDialog(this,"Error","Invalid Email/Password")
                    }
                })
                val checkLiveData = MutableLiveData<Boolean>()
                lifecycleScope.launch {
                    mainViewModel.loginData.observe(this@MainActivity,{
                        if (it!=null){
                            checkLiveData.value=true
                            mainViewModel.saveSession(UserModel(it.userId.toString(),it.token.toString()))
                        }
                    }
                    )
                    checkLiveData.observe(this@MainActivity){
                        if (it==true){
                            lifecycleScope.launch {
                                mainViewModel.getSession().observe(this@MainActivity){
                                    if (it!=null){
                                        val intent=Intent(this@MainActivity, StoryActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    }
                                }
                            }
                        }
                    }
                }

            }else{
                val name = binding.edRegisterName.text.toString()
                val email = binding.edRegisterEmail.text.toString()
                val password = binding.edRegisterPassword.text.toString()
                if(name==""||email==""||password=="") {
                    showDialog(this,"Error","Please fill all fields")
                }else{
                    mainViewModel.register(name, email, password)

                }

            }
        }
        lifecycleScope.launch {
            mainViewModel.Message.observe(this@MainActivity, Observer { Message ->
                if (Message != null) {
                    showDialog(this@MainActivity,"Notice",Message)
                    if (Message=="User created"){
                        setView()
                    }
                }
            })
        }
    }

    private fun showLoading(load:Boolean){
        if (load){
            binding.loadingMain.visibility= View.VISIBLE
        }else{
            binding.loadingMain.visibility= View.GONE
        }
    }

    fun showDialog(context: Context,errorTitle:String, errorMessage: String) {
        AlertDialog.Builder(context)
            .setTitle(errorTitle)
            .setMessage(errorMessage)
            .setPositiveButton("Ok") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun setView(){
        if (login_page){
            binding.NameEditTextLayout.visibility=View.VISIBLE
            binding.tvAuthChange.text= getString(R.string.auth_login)
            binding.btnLogin.text="Register"
            binding.RegisterEmailEditTextLayout.visibility=View.VISIBLE
            binding.NameEditTextLayout.visibility=View.VISIBLE
            binding.RegisterPasswordEditTextLayout.visibility=View.VISIBLE
            binding.LoginEmailEditTextLayout.visibility=View.GONE
            binding.LoginPasswordEditTextLayout.visibility=View.GONE
            login_page=false
        }else{
            binding.NameEditTextLayout.visibility=View.GONE
            binding.tvAuthChange.text= getString(R.string.auth_register)
            binding.btnLogin.text="Login"
            binding.RegisterEmailEditTextLayout.visibility=View.GONE
            binding.NameEditTextLayout.visibility=View.GONE
            binding.RegisterPasswordEditTextLayout.visibility=View.GONE
            binding.LoginEmailEditTextLayout.visibility=View.VISIBLE
            binding.LoginPasswordEditTextLayout.visibility=View.VISIBLE
            login_page=true
        }
    }
}