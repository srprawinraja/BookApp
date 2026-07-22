package com.example.bookapp.ui.bookdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookapp.data.ApiService
import com.example.bookapp.data.db.book.BookEntity
import com.example.bookapp.data.db.book.BookRepository
import com.example.bookapp.ui.model.Book
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookDetailViewModel @Inject constructor(
    private val repository: BookRepository,
    private val apiService: ApiService
) : ViewModel() {
    private val _state = MutableStateFlow(BookDetailState())
    val state: StateFlow<BookDetailState> = _state.asStateFlow()

    fun onEvent(event: BookDetailEvent) {
        when (event) {
            is BookDetailEvent.LoadSummary -> {
                fetchSummary(event.bookKey)
            }
            is BookDetailEvent.OnSaveToggle -> {
                toggleSaveBook(event.book)
            }
            is BookDetailEvent.CheckIfSaved -> {
                checkIfSaved(event.bookKey)
            }
        }
    }

    private fun checkIfSaved(bookKey: String) {
        viewModelScope.launch {
            val isSaved = repository.isBookSaved(bookKey)
            _state.update { it.copy(isSaved = isSaved) }
        }
    }

    private fun toggleSaveBook(book: Book) {
        viewModelScope.launch {
            val isSaved = repository.isBookSaved(book.key)
            if (isSaved) {
                repository.deleteBook(
                    BookEntity(
                        key = book.key,
                        title = book.title,
                        author = book.author,
                        coverUrl = book.coverUrl,
                        summary = _state.value.summary
                    )
                )
            } else {
                repository.saveBook(
                    BookEntity(
                        key = book.key,
                        title = book.title,
                        author = book.author,
                        coverUrl = book.coverUrl,
                        summary = _state.value.summary
                    )
                )
            }
            _state.update { it.copy(isSaved = !isSaved) }
        }
    }

    private fun fetchSummary(bookKey: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val response = apiService.getWorkDetails(bookKey)
                val summaryText = response.getDescriptionText()
                _state.update { 
                    it.copy(
                        summary = summaryText,
                        isLoading = false 
                    ) 
                }
            } catch (e: Exception) {
                _state.update { 
                    it.copy(
                        summary = "Failed to load summary. It might not be available for this book.",
                        isLoading = false 
                    ) 
                }
            }
        }
    }
}
