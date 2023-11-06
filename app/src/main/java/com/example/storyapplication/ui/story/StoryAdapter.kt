package com.example.storyapplication.ui.story

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyapplication.data.remote.response.ListStoryItem
import com.example.storyapplication.databinding.ItemLayoutBinding
import com.example.storyapplication.ui.detail.DetailStoryActivity

class StoryAdapter:PagingDataAdapter<ListStoryItem,StoryAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val story = getItem(position)
        Log.d("hello",story.toString())
        if (story != null) {
            holder.bind(story)
        }
    }

    class MyViewHolder(private val binding: ItemLayoutBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(story: ListStoryItem){
            Glide.with(binding.root)
                .load(story.photoUrl)
                .centerCrop()
                .into(binding.ivItem)
            binding.tvItemTitle.text=story.name.toString()
            binding.tvItemDescription.text=story.description.toString()
            binding.root.setOnClickListener {
                val intent = Intent(binding.root.context, DetailStoryActivity::class.java)
                intent.putExtra("image", story.photoUrl)
                intent.putExtra("title", story.name.toString())
                intent.putExtra("description", story.description.toString())
                val optionsCompat: ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    itemView.context as Activity,
                    Pair(binding.ivItem,"image"),
                    Pair(binding.tvItemTitle,"title"),
                    Pair(binding.tvItemDescription,"description"),
                )
                binding.root.context.startActivity(intent,optionsCompat.toBundle())
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }
            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

}