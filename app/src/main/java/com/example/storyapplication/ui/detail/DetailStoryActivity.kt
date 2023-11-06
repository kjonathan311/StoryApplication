package com.example.storyapplication.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.storyapplication.R
import com.example.storyapplication.databinding.ActivityDetailStoryBinding

class DetailStoryActivity : AppCompatActivity() {

    lateinit var binding:ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imgIntent=intent.getStringExtra("image")
        val titleIntent=intent.getStringExtra("title")
        val descriptionIntent=intent.getStringExtra("description")
        Glide.with(applicationContext)
            .load(imgIntent)
            .centerCrop()
            .into(binding.ivDetailStory)
        binding.tvDetailTitle.text=titleIntent
        binding.tvDetailDescription.text=descriptionIntent
    }
}