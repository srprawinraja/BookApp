package com.example.bookapp.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.example.bookapp.ui.component.BookRow
import com.example.bookapp.ui.component.CategoryRow
import com.example.bookapp.ui.model.Book
import kotlin.math.absoluteValue

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onBookClick: (Book) -> Unit = {},
    onViewAllClick: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()
    
    val bannerImages = listOf(
        "https://images.unsplash.com/photo-1507842217343-583bb7270b66?auto=format&fit=crop&w=800&q=80",
        "https://images.unsplash.com/photo-1512820790803-83ca734da794?auto=format&fit=crop&w=800&q=80",
        "https://images.unsplash.com/photo-1495446815901-a7297e633e8d?auto=format&fit=crop&w=800&q=80"
    )

    val infinitePageCount = Int.MAX_VALUE
    val initialPage = (infinitePageCount / 2)
    val pagerState = rememberPagerState(
        initialPage = initialPage,
        pageCount = { infinitePageCount }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(top = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 32.dp),
            pageSpacing = 16.dp,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) { page ->
            val actualPage = page % bannerImages.size
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .graphicsLayer {
                        val pageOffset = (
                                (pagerState.currentPage - page) + pagerState
                                    .currentPageOffsetFraction
                                ).absoluteValue

                        val scale = 0.85f + (1f - 0.85f) * (1f - pageOffset.coerceIn(0f, 1f))
                        scaleX = scale
                        scaleY = scale

                        alpha = 0.5f + (1f - 0.5f) * (1f - pageOffset.coerceIn(0f, 1f))
                    }
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    AsyncImage(
                        model = bannerImages[actualPage],
                        contentDescription = "Banner Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.3f))
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = when(actualPage) {
                                0 -> "Summer Book Sale"
                                1 -> "New Arrivals"
                                else -> "Best Sellers 2024"
                            },
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Up to 50% Off",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        CategoryRow(
            selectedCategory = state.selectedCategory,
            onCategoryClick = { category ->
                viewModel.onEvent(HomeEvent.OnCategorySelected(category))
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Popular Books",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color.Yellow)
            }
        } else {
            BookRow(books = state.homeBooks, onBookClick = onBookClick)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onViewAllClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Yellow,
                contentColor = Color.Black
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "View All", fontWeight = FontWeight.Bold)
        }
        
        Spacer(modifier = Modifier.height(32.dp))
    }
}
