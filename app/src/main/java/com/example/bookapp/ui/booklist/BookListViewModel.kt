package com.example.bookapp.ui.booklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookapp.data.RetroFitInstance
import com.example.bookapp.ui.model.Book
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BookListViewModel : ViewModel() {
    private val _state = MutableStateFlow(BookListState())
    val state: StateFlow<BookListState> = _state.asStateFlow()

    init {
        onEvent(BookListEvent.LoadMoreBooks)
    }

    fun onEvent(event: BookListEvent) {
        when (event) {
            BookListEvent.LoadMoreBooks -> fetchMoreBooks()
        }
    }

    private fun fetchMoreBooks() {
        if (_state.value.isMoreLoading) return

        viewModelScope.launch {
            _state.update { it.copy(isMoreLoading = true) }
            try {
                val nextPage = _state.value.currentPage
                val response = RetroFitInstance.api.searchBooks(limit = 10, page = nextPage)
                val newBooks = response.docs.map { doc ->
                    Book(
                        key = doc.key,
                        title = doc.title,
                        author = doc.getAuthor(),
                        coverUrl = doc.getCoverUrl()
                    )
                }
                _state.update {
                    it.copy(
                        books = it.books + newBooks,
                        isMoreLoading = false,
                        currentPage = nextPage + 1
                    )
                }
            } catch (e: Exception) {
                _state.update { it.copy(isMoreLoading = false) }
            }
        }
    }
}
