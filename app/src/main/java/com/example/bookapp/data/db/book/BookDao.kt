package com.example.bookapp.data.db.book

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Query("SELECT * FROM books ORDER BY savedAt DESC")
    fun getAllBooks(): Flow<List<BookEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBook(book: BookEntity)

    @Delete
    fun deleteBook(book: BookEntity)

    @Query("SELECT COUNT(*) FROM books WHERE `key` = :bookKey")
    fun isBookSaved(bookKey: String): Int
}
