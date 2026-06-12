package com.example.bookapp.ui.main

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Search : Screen("search")
    object Library : Screen("library")
    object Account : Screen("account")
    object BookList : Screen("book_list")
    object BookDetail : Screen("book_detail/{bookKey}/{title}/{author}/{coverUrl}") {
        fun createRoute(bookKey: String, title: String, author: String, coverUrl: String): String {
            val encodedKey = java.net.URLEncoder.encode(bookKey, "UTF-8")
            val encodedTitle = java.net.URLEncoder.encode(title, "UTF-8")
            val encodedAuthor = java.net.URLEncoder.encode(author, "UTF-8")
            val encodedCover = java.net.URLEncoder.encode(coverUrl, "UTF-8")
            return "book_detail/$encodedKey/$encodedTitle/$encodedAuthor/$encodedCover"
        }
    }
}
