package com.example.bookapp.ui.search

import com.example.bookapp.ui.model.Book

sealed class SearchEvent {
    data class OnQueryChanged(val query: String) : SearchEvent()
    data class OnCategorySelected(val category: String) : SearchEvent()
}

data class SearchState(
    val query: String = "",
    val selectedCategory: String = "All",
    val searchResults: List<Book> = emptyList(),
    val isLoading: Boolean = false
)
