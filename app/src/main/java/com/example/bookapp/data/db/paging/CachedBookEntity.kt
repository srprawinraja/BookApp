package com.example.bookapp.data.db.paging

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cached_books")
data class CachedBookEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val key: String,
    val title: String,
    val author: String,
    val coverUrl: String,
    val page: Int
)
