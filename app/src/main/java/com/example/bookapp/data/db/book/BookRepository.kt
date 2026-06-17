package com.example.bookapp.data.db.book

import kotlinx.coroutines.flow.Flow

class BookRepository(private val bookDao: BookDao) {

    fun getAllBooks(): Flow<List<BookEntity>> = bookDao.getAllBooks()

    suspend fun saveBook(book: BookEntity) {
        bookDao.insertBook(book)
    }

    suspend fun deleteBook(book: BookEntity) {
        bookDao.deleteBook(book)
    }

    suspend fun isBookSaved(key: String): Boolean {
        return bookDao.isBookSaved(key)
    }
}
