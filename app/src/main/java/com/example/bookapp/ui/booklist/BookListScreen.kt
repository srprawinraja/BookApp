package com.example.bookapp.ui.booklist

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.example.bookapp.ui.component.VerticalBookItem
import com.example.bookapp.ui.model.Book

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookListScreen(
    viewModel: BookListViewModel = viewModel(),
    onBookClick: (Book) -> Unit,
    onBackClick: () -> Unit
) {
    val pagingItems = viewModel.books.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("All Books") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        containerColor = Color.White
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                count = pagingItems.itemCount,
                key = pagingItems.itemKey { it.key }
            ) { index ->
                val book = pagingItems[index]
                if (book != null) {
                    VerticalBookItem(book = book, onClick = onBookClick)
                }
            }

            // Handle loading state for the footer
            when (val appendState = pagingItems.loadState.append) {
                is LoadState.Loading -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = Color.Yellow)
                        }
                    }
                }
                is LoadState.Error -> {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "Error loading more books", color = Color.Red)
                            Button(
                                onClick = { pagingItems.retry() },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Yellow)
                            ) {
                                Text("Retry", color = Color.Black)
                            }
                        }
                    }
                }
                else -> {}
            }
        }

        // Handle initial load state
        when (val refreshState = pagingItems.loadState.refresh) {
            is LoadState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color.Yellow)
                }
            }
            is LoadState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Failed to load books", color = Color.Red)
                        Button(
                            onClick = { pagingItems.retry() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Yellow)
                        ) {
                            Text("Retry", color = Color.Black)
                        }
                    }
                }
            }
            else -> {}
        }
    }
}
