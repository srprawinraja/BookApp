package com.example.bookapp.ui.bookdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookapp.data.RetroFitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BookDetailViewModel : ViewModel() {
    private val _state = MutableStateFlow(BookDetailState())
    val state: StateFlow<BookDetailState> = _state.asStateFlow()

    fun onEvent(event: BookDetailEvent) {
        when (event) {
            is BookDetailEvent.LoadSummary -> {
                fetchSummary(event.bookKey)
            }
        }
    }

    private fun fetchSummary(bookKey: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val response = RetroFitInstance.api.getWorkDetails(bookKey)
                _state.update { 
                    it.copy(
                        summary = response.getDescriptionText(),
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
