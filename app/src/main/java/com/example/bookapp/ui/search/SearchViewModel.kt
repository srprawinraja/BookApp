package com.example.bookapp.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookapp.data.ApiService
import com.example.bookapp.ui.model.Book
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val apiService: ApiService
) : ViewModel() {
    private val _state = MutableStateFlow(SearchState())
    val state: StateFlow<SearchState> = _state.asStateFlow()

    private var searchJob: Job? = null

    init {
        performSearch(_state.value.selectedCategory, "")
    }

    fun onEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.OnQueryChanged -> {
                _state.update { it.copy(query = event.query) }
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    delay(500) // Debounce for 500ms
                    performSearch(_state.value.selectedCategory, event.query)
                }
            }
            is SearchEvent.OnCategorySelected -> {
                _state.update { it.copy(selectedCategory = event.category) }
                performSearch(event.category, _state.value.query)
            }
        }
    }

    private fun performSearch(category: String, query: String) {
        val searchQuery = if (query.isNotBlank()) {
            query
        } else if (category == "All") {
            "book"
        } else {
            category
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val response = apiService.searchBooks(query = searchQuery, limit = 20)
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
