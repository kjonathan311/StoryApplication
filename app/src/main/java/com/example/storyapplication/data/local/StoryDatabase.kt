package com.example.storyapplication.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.storyapplication.data.remote.response.ListStoryItem

@Database(
    entities = [ListStoryItem::class,RemoteKeys::class],
    version = 2,
    exportSchema = false
)
abstract class StoryDatabase : RoomDatabase() {
    abstract fun storyDao(): StoryDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}