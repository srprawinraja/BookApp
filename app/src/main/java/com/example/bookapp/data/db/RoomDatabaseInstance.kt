package com.example.bookapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.bookapp.data.db.book.BookDao
import com.example.bookapp.data.db.book.BookEntity
import com.example.bookapp.data.db.paging.CachedBookEntity
import com.example.bookapp.data.db.paging.PagingDao
import com.example.bookapp.data.db.paging.RemoteKeys

@Database(entities = [BookEntity::class, CachedBookEntity::class, RemoteKeys::class], version = 3, exportSchema = false)
abstract class BookDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
    abstract fun pagingDao(): PagingDao

    companion object {
        @Volatile
        private var INSTANCE: BookDatabase? = null

        fun getDatabase(context: Context): BookDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BookDatabase::class.java,
                    "book_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
