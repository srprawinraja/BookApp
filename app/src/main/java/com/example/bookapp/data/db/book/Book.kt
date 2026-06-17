package com.example.bookapp.data.db.book

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class BookEntity(
    @PrimaryKey val key: String,
    val title: String,
    val author: String,
    val coverUrl: String,
    val summary: String,
    val savedAt: Long = System.currentTimeMillis()
)
