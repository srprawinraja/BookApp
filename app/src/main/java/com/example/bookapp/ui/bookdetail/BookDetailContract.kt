package com.example.bookapp.ui.bookdetail

import com.example.bookapp.ui.model.Book

sealed class BookDetailEvent {
    data class LoadSummary(val bookKey: String) : BookDetailEvent()
}

data class BookDetailState(
    val book: Book? = null,
    val summary: String = "Loading summary...",
    val isLoading: Boolean = false
)
