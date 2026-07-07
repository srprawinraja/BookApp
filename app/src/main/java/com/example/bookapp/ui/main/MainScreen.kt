package com.example.bookapp.ui.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.bookapp.ui.account.AccountScreen
import com.example.bookapp.ui.bookdetail.BookDetailScreen
import com.example.bookapp.ui.booklist.BookListScreen
import com.example.bookapp.ui.library.LibraryScreen
import com.example.bookapp.ui.home.HomeScreen
import com.example.bookapp.ui.model.Book
import com.example.bookapp.ui.search.SearchScreen

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val items = listOf(
        NavigationItem.Home,
        NavigationItem.Search,
        NavigationItem.Library,
        NavigationItem.Account
    )

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                contentColor = Color.Black
            ) {
                items.forEach { item ->
                    val isSelected = currentDestination?.hierarchy?.any { it.route == item.screen.route } == true ||
                            (item == NavigationItem.Home && currentDestination?.route == Screen.BookList.route)
                    
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.title
                            )
                        },
                        label = { Text(text = item.title) },
                        selected = isSelected,
                        onClick = {
                            navController.navigate(item.screen.route)
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF1A73E8),
                            selectedTextColor = Color(0xFF1A73E8),
                            indicatorColor = Color.Transparent,
                            unselectedIconColor = Color.Black,
                            unselectedTextColor = Color.Black
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) { 
                HomeScreen(
                    onBookClick = { book ->
                        navController.navigate(Screen.BookDetail.createRoute(book.key, book.title, book.author, book.coverUrl))
                    },
                    onViewAllClick = { navController.navigate(Screen.BookList.route) }
                ) 
            }
            composable(Screen.Search.route) { 
                SearchScreen(
                    onBookClick = { book ->
                        navController.navigate(Screen.BookDetail.createRoute(book.key, book.title, book.author, book.coverUrl))
                    }
                ) 
            }
            composable(Screen.Library.route) { 
                LibraryScreen(
                    onBookClick = { book ->
                        navController.navigate(Screen.BookDetail.createRoute(book.key, book.title, book.author, book.coverUrl))
                    }
                ) 
            }
            composable(Screen.Account.route) { AccountScreen() }
            composable(Screen.BookList.route) { 
                BookListScreen(
                    onBookClick = { book ->
                        navController.navigate(Screen.BookDetail.createRoute(book.key, book.title, book.author, book.coverUrl))
                    },
                    onBackClick = { navController.popBackStack() }
                )
            }
            composable(
                route = Screen.BookDetail.route,
                arguments = listOf(
                    navArgument("bookKey") { type = NavType.StringType },
                    navArgument("title") { type = NavType.StringType },
                    navArgument("author") { type = NavType.StringType },
                    navArgument("coverUrl") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val bookKey = backStackEntry.arguments?.getString("bookKey") ?: ""
                val title = backStackEntry.arguments?.getString("title") ?: ""
                val author = backStackEntry.arguments?.getString("author") ?: ""
                val coverUrl = backStackEntry.arguments?.getString("coverUrl") ?: ""
                
                BookDetailScreen(
                    book = Book(bookKey, title, author, coverUrl),
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}

sealed class NavigationItem(val screen: Screen, val icon: ImageVector, val title: String) {
    object Home : NavigationItem(Screen.Home, Icons.Default.Home, "Home")
    object Search : NavigationItem(Screen.Search, Icons.Default.Search, "Search")
    object Library : NavigationItem(Screen.Library, Icons.AutoMirrored.Filled.List, "Library")
    object Account : NavigationItem(Screen.Account, Icons.Default.Person, "Account")
}
