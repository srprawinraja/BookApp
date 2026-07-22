package com.example.bookapp.di

import android.content.Context
import com.example.bookapp.data.db.BookDatabase
import com.example.bookapp.data.db.book.BookDao
import com.example.bookapp.data.db.paging.PagingDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): BookDatabase {
        return BookDatabase.getDatabase(context)
    }

    @Provides
    fun provideBookDao(database: BookDatabase): BookDao {
        return database.bookDao()
    }

    @Provides
    fun providePagingDao(database: BookDatabase): PagingDao {
        return database.pagingDao()
    }
}
