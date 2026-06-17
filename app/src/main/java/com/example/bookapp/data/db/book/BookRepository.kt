package com.example.bookapp.data.db.book

import android.content.Context
import com.example.bookapp.data.db.BookDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class BookRepository(private val bookDao: BookDao) {

    fun getAllBooks(): Flow<List<BookEntity>> = bookDao.getAllBooks()

    suspend fun saveBook(book: BookEntity) {
        withContext(Dispatchers.IO) {
            bookDao.insertBook(book)
        }
    }

    suspend fun deleteBook(book: BookEntity) {
        withContext(Dispatchers.IO) {
            bookDao.deleteBook(book)
        }
    }

    suspend fun isBookSaved(key: String): Boolean {
        return withContext(Dispatchers.IO) {
            bookDao.isBookSaved(key) > 0
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: BookRepository? = null

        fun getInstance(context: Context): BookRepository {
            return INSTANCE ?: synchronized(this) {
                val database = BookDatabase.getDatabase(context)
                val instance = BookRepository(database.bookDao())
                INSTANCE = instance
                instance
            }
        }
    }
}
