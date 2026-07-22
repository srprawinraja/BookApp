package com.example.bookapp.ui.booklist

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.bookapp.data.ApiService
import com.example.bookapp.data.db.BookDatabase
import com.example.bookapp.data.db.paging.CachedBookEntity
import com.example.bookapp.data.db.paging.RemoteKeys
import java.io.IOException
import javax.inject.Inject
import retrofit2.HttpException

@OptIn(ExperimentalPagingApi::class)
class BookRemoteMediator @Inject constructor(
    private val database: BookDatabase,
    private val apiService: ApiService
) : RemoteMediator<Int, CachedBookEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CachedBookEntity>
    ): MediatorResult {

        val page = when (loadType) {
            LoadType.REFRESH -> 1
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        try {
            val response = apiService.searchBooks(
                query = "book",
                limit = state.config.pageSize,
                page = page
            )

            val books = response.docs.distinctBy { it.key }
            val endOfPaginationReached = books.isEmpty()

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.pagingDao().clearRemoteKeys()
                    database.pagingDao().clearAllBooks()
                }
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = books.map {
                    RemoteKeys(bookKey = it.key, prevKey = prevKey, nextKey = nextKey)
                }
                database.pagingDao().insertAllKeys(keys)
                database.pagingDao().insertAll(books.map {
                    CachedBookEntity(
                        key = it.key,
                        title = it.title,
                        author = it.getAuthor(),
                        coverUrl = it.getCoverUrl(),
                        page = page
                    )
                })
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            android.util.Log.e("BookRemoteMediator", "IOException: ${exception.message}", exception)
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            android.util.Log.e("BookRemoteMediator", "HttpException: ${exception.message()}", exception)
            return MediatorResult.Error(exception)
        } catch (exception: Exception) {
            android.util.Log.e("BookRemoteMediator", "Unexpected Exception: ${exception.message}", exception)
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, CachedBookEntity>): RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { book ->
                database.pagingDao().remoteKeysBookId(book.key)
            }
    }


}
