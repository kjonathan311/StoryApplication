package com.example.storyapplication.ui.story

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapplication.R
import com.example.storyapplication.databinding.ActivityStoryBinding
import com.example.storyapplication.ui.main.MainActivity
import com.example.storyapplication.ui.map.MapsActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class StoryActivity : AppCompatActivity() {

    private val storyViewModel: StoryViewModel by viewModel()


    lateinit var binding:ActivityStoryBinding
    private  var addNewStory=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showLoading(true)
        storyViewModel.getSession().observe(this){
            if (!it.isLogin) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }else{
                setSupportActionBar(binding.toolbar)
                binding.abAddStory.visibility=View.VISIBLE
                storyViewModel.setLoading(false)
                setData()
            }
        }
        binding.rvListStory.layoutManager =LinearLayoutManager(this)
        binding.abAddStory.setOnClickListener {
            val intent=Intent(this,AddStoryActivity::class.java)
            startActivity(intent)
            addNewStory=true
        }
        if (addNewStory){
            setData()
            storyViewModel.refreshList()
            addNewStory=false

        }
        storyViewModel.loading.observe(this) {
            showLoading(it)
        }
    }

    override fun onResume() {
        super.onResume()
        storyViewModel.refreshList()
    }

    private fun setData(){
        val adapter= StoryAdapter()
        binding.rvListStory.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        storyViewModel.listStory.observe(this) {pagingData->
            adapter.submitData(lifecycle, pagingData)
            binding.rvListStory.scrollToPosition(0)
        }
    }

    private fun showLoading(load:Boolean){
        if (load){
            binding.loadingStory.visibility= View.VISIBLE
        }else{
            binding.loadingStory.visibility= View.GONE
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_map->{
                startActivity(Intent(this,MapsActivity::class.java))
            }
            R.id.action_logout->{
                storyViewModel.logout()
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}