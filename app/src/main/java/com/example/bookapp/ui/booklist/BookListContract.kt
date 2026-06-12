package com.example.bookapp.ui.booklist

import com.example.bookapp.ui.model.Book

sealed class BookListEvent {
    object LoadMoreBooks : BookListEvent()
}

data class BookListState(
    val books: List<Book> = emptyList(),
    val isMoreLoading: Boolean = false,
    val currentPage: Int = 1
)
