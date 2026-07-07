package com.example.bookapp.data.db.paging

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PagingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(books: List<CachedBookEntity>)

    @Query("SELECT * FROM cached_books ORDER BY id ASC")
    fun getPagedBooks(): PagingSource<Int, CachedBookEntity>

    @Query("DELETE FROM cached_books")
    suspend fun clearAllBooks()

    // Remote Keys
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllKeys(remoteKey: List<RemoteKeys>)

    @Query("SELECT * FROM remote_keys WHERE bookKey = :bookKey")
    suspend fun remoteKeysBookId(bookKey: String): RemoteKeys?

    @Query("DELETE FROM remote_keys")
    suspend fun clearRemoteKeys()
}
