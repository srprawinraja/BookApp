package com.example.bookapp.ui.library

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookapp.data.db.book.BookRepository
import com.example.bookapp.ui.model.Book
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LibraryViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = BookRepository.getInstance(application)
    private val _state = MutableStateFlow(LibraryState())
    val state: StateFlow<LibraryState> = _state.asStateFlow()

    init {
        fetchSavedBooks()
    }

    private fun fetchSavedBooks() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            repository.getAllBooks().collectLatest { entities ->
                val books = entities.map { entity ->
                    Book(
                        key = entity.key,
                        title = entity.title.replace("+", " "),
                        author = entity.author.replace("+", " "),
                        coverUrl = entity.coverUrl
                    )
                }
                _state.update { it.copy(savedBooks = books, isLoading = false) }
            }
        }
    }
}

data class LibraryState(
    val savedBooks: List<Book> = emptyList(),
    val isLoading: Boolean = false
)
