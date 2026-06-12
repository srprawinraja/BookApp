package com.example.bookapp.ui.home

import com.example.bookapp.ui.model.Book

sealed class HomeEvent {
    object LoadHomeBooks : HomeEvent()
    data class OnCategorySelected(val category: String) : HomeEvent()
}

data class HomeState(
    val homeBooks: List<Book> = emptyList(),
    val isLoading: Boolean = false,
    val selectedCategory: String = "All"
)
