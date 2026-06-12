package com.example.bookapp.data

import com.example.bookapp.data.model.BookResponse
import com.example.bookapp.data.model.WorkResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search.json")
    suspend fun searchBooks(
        @Query("q") query: String = "book",
        @Query("limit") limit: Int = 10,
        @Query("page") page: Int = 1
    ): BookResponse

    @GET("{key}.json")
    suspend fun getWorkDetails(
        @Path("key", encoded = true) key: String
    ): WorkResponse
}
