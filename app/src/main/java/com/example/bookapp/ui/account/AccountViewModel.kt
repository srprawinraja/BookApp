package com.example.bookapp.ui.account

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(AccountState())
    val state: StateFlow<AccountState> = _state.asStateFlow()
}

data class AccountState(
    val name: String = "John Doe",
    val profileImageUrl: String = "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?auto=format&fit=crop&w=150&q=80"
)
