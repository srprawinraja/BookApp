package com.example.bookapp.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookapp.data.RetroFitInstance
import com.example.bookapp.ui.model.Book
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {
    private val _state = MutableStateFlow(SearchState())
    val state: StateFlow<SearchState> = _state.asStateFlow()

    init {
        performSearch(_state.value.selectedCategory)
    }

    fun onEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.OnQueryChanged -> {
                _state.update { it.copy(query = event.query) }
            }
            is SearchEvent.OnCategorySelected -> {
                _state.update { it.copy(selectedCategory = event.category) }
                performSearch(event.category)
            }
        }
    }

    private fun performSearch(category: String) {
        val query = if (category == "All") "book" else category
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val response = RetroFitInstance.api.searchBooks(query = query, limit = 20)
                val books = response.docs.map { doc ->
                    Book(
                        key = doc.key,
                        title = doc.title.replace("+", " "),
                        author = doc.getAuthor().replace("+", " "),
                        coverUrl = doc.getCoverUrl()
                    )
                }
                _state.update { it.copy(searchResults = books, isLoading = false) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }
}
