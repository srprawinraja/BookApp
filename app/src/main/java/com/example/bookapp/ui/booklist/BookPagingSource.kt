package com.example.bookapp.ui.booklist

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.bookapp.data.RetroFitInstance
import com.example.bookapp.data.model.BookDoc

class BookPagingSource : PagingSource<Int, BookDoc>() {

    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, BookDoc> {
        return try {
            val page = params.key ?: 1
            val response = RetroFitInstance.api.searchBooks(limit = params.loadSize, page = page)
            val books = response.docs

            LoadResult.Page(
                data = books,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (books.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(
        state: PagingState<Int, BookDoc>
    ): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
