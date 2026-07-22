package com.example.bookapp.ui.booklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.bookapp.data.db.paging.PagingDao
import com.example.bookapp.ui.model.Book
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
@HiltViewModel
class BookListViewModel @Inject constructor(
    private val pagingDao: PagingDao,
    private val remoteMediator: BookRemoteMediator
) : ViewModel() {

    val books: Flow<PagingData<Book>> = Pager(
        config = PagingConfig(
            pageSize = 10,
            prefetchDistance = 2,
            enablePlaceholders = false
        ),
        remoteMediator = remoteMediator,
        pagingSourceFactory = { pagingDao.getPagedBooks() }
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
