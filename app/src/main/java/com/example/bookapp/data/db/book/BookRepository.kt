package com.example.bookapp.data.db.book

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookRepository @Inject constructor(private val bookDao: BookDao) {

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
}
