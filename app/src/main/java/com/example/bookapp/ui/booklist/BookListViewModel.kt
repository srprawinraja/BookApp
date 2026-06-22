package com.example.bookapp.ui.booklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.bookapp.ui.model.Book
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BookListViewModel : ViewModel() {

    val books: Flow<PagingData<Book>> = Pager(
        config = PagingConfig(
            pageSize = 10,
            prefetchDistance = 2,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { BookPagingSource() }
    ).flow
        .map { pagingData ->
            pagingData.map { doc ->
                Book(
                    key = doc.key,
                    title = doc.title.replace("+", " "),
                    author = doc.getAuthor().replace("+", " "),
                    coverUrl = doc.getCoverUrl()
                )
            }
        }
        .cachedIn(viewModelScope)
}
