package com.example.bookapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookapp.data.ApiService
import com.example.bookapp.ui.model.Book
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val apiService: ApiService
) : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    init {
        onEvent(HomeEvent.LoadHomeBooks)
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            HomeEvent.LoadHomeBooks -> fetchHomeBooks(_state.value.selectedCategory)
            is HomeEvent.OnCategorySelected -> {
                _state.update { it.copy(selectedCategory = event.category) }
                fetchHomeBooks(event.category)
            }
        }
    }

    private fun fetchHomeBooks(category: String) {
        val query = if (category == "All") "book" else category
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val response = apiService.searchBooks(query = query, limit = 10)
                val books = response.docs.map { doc ->
                    Book(
                        key = doc.key,
                        title = doc.title.replace("+", " "),
                        author = doc.getAuthor().replace("+", " "),
                        coverUrl = doc.getCoverUrl()
                    )
                }
                _state.update { it.copy(homeBooks = books, isLoading = false) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }
}
