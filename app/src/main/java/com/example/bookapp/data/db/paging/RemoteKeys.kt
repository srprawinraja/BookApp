package com.example.bookapp.data.db.paging

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeys(
    @PrimaryKey val bookKey: String,
    val prevKey: Int?,
    val nextKey: Int?
)
