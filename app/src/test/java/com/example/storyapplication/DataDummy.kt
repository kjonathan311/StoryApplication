package com.example.storyapplication

import com.example.storyapplication.data.remote.response.ListStoryItem
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object DataDummy {
    fun generateDummyStoryListResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(id = i.toString(), createdAt = getCurrentTime(), name = "name $i", description = "description $i")
            items.add(story)
        }
        return items
    }
}

fun getCurrentTime(): String {
    val currentDateTime = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    return currentDateTime.format(formatter)
}
