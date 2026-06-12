package com.example.bookapp.data.model

import com.google.gson.annotations.SerializedName

data class BookResponse(
    @SerializedName("docs")
    val docs: List<BookDoc>
)

data class BookDoc(
    @SerializedName("key")
    val key: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("author_name")
    val authorName: List<String>?,
    @SerializedName("cover_i")
    val coverI: Int?
) {
    fun getCoverUrl(): String {
        return if (coverI != null) {
            "https://covers.openlibrary.org/b/id/$coverI-M.jpg"
        } else {
            "https://via.placeholder.com/150"
        }
    }
    
    fun getAuthor(): String {
        return authorName?.firstOrNull() ?: "Unknown Author"
    }
}
