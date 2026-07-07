package com.example.bookapp.ui.booklist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.bookapp.data.db.BookDatabase
import com.example.bookapp.ui.model.Book
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalPagingApi::class)
class BookListViewModel(application: Application) : AndroidViewModel(application) {

    private val database = BookDatabase.getDatabase(application)

    val books: Flow<PagingData<Book>> = Pager(
        config = PagingConfig(
            pageSize = 10,
            prefetchDistance = 2,
            enablePlaceholders = false
        ),
        remoteMediator = BookRemoteMediator(database),
        pagingSourceFactory = { database.pagingDao().getPagedBooks() }
    ).flow
        .map { pagingData ->
            pagingData.map { entity ->
                Book(
                    key = entity.key,
                    title = entity.title.replace("+", " "),
                    author = entity.author.replace("+", " "),
                    coverUrl = entity.coverUrl
                )
            }
        }
        .cachedIn(viewModelScope)
}
