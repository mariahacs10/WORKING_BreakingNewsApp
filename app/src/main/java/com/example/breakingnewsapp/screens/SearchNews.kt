package com.example.breakingnewsapp.screens

import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection.Companion.In
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.breakingnewsapp.api.RetrofitInstanceClass
import com.example.breakingnewsapp.models.Article
import com.example.breakingnewsapp.models.ArticleViewModel

/**Add the destination screens, These will be simple functions that do nothing more than display the icon
for the corresponding bar item selection. We will declare each screen composable in a separate file, each of which will
be placed in a new package named screens.*/

// This is a Composable function that handles navigation within the app.
@Composable
fun AppNavigator2(viewModel: ArticleViewModel) {
    // Creates a NavController to handle navigation
    val navController = rememberNavController()

    // NavHost manages navigation between different destinations
    NavHost(navController, startDestination = "articleList") {
        // Destination to show list of articles
        composable("articleList") {
            SearchNews(viewModel, navController)
        }

        // Destination to show full article
        composable("fullArticle") {
            FullArticleScreenSearch(viewModel)
        }
    }
}


// This Composable function displays the full article using a WebView.
@Composable
fun FullArticleScreenSearch(viewModel: ArticleViewModel) {
    // Get selected article from view model
    val article = viewModel.selectedArticle.value

    Box(modifier = Modifier.fillMaxSize()) {
        // Show article content in WebView
        AndroidView(factory = { ctx ->
            WebView(ctx).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                webViewClient = WebViewClient()
                article?.url?.let { loadUrl(it) }
            }
        }, update = { view ->
            viewModel.selectedArticle.value?.url?.let { view.loadUrl(it) }
        })

        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 78.dp),
            //This makes the heart clickable
            //and saved to the database
            onClick = {  article?.let {
                viewModel.saveArticle(it)
            }}
        ) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "Save Article"
            )
        }
    }
}



// Card to display single article
@Composable
fun SearchNewsCard(
    navController: NavController,
    article: Article,
    viewModel: ArticleViewModel

) {

    // Clicking card navigates to full article screen
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(18.dp)
            .clickable {
                //The clickable modifier makes the element clickable by detecting click events.
                // here it is added to the Card composable.
                viewModel.selectedArticle.value = article
                /**This line updates the selectedArticle state in the ArticleViewModel viewModel to the current article being displayed. So when the card is clicked, it will save a
                 * reference to that article object in the view model.*/
                navController.navigate("fullArticle")
            }
    ) {

        Column(modifier = Modifier.fillMaxSize()) {
            CoilImage(
                imageUrl = article.urlToImage ?: "",
                contentDescription = article.title,
                modifier = Modifier
                    .size(300.dp, 300.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            article.title?.let { Text(text = it, style = MaterialTheme.typography.bodyMedium) }

            Spacer(modifier = Modifier.height(8.dp))

            article.description?.let { Text(text = it) }
        }
    }
}




/**6. This @Composable function displays a list of news articles. It uses a LazyColumn to display a list of BreakingNewsCard*/
@Composable
fun SearchNewsList(navController: NavController, articles: List<Article>, viewModel: ArticleViewModel) {
    // LazyColumn displays list of cards
    LazyColumn {
        items(articles) { article ->
            SearchNewsCard(navController, article, viewModel)
        }
    }
}



@Composable
fun SearchNews(
    viewModel: ArticleViewModel,
    navController: NavController
) {
   var searchQuery by remember { mutableStateOf("") }

    val articles = remember { mutableStateOf<List<Article>>(emptyList()) }
    LaunchedEffect(searchQuery) {
        if (searchQuery.isNotEmpty()) {
            val response = RetrofitInstanceClass.api.searchForNews(searchQuery)
            articles.value = response.body()?.articles ?: emptyList()
        }
    }


    Column(modifier = Modifier.fillMaxSize()) {
        EditTextMethod(
            searchQuery = searchQuery,
            onSearchQueryChange = { searchQuery = it }
        )
        SearchNewsList(navController, articles.value, viewModel)
    }
}

/**In EditTextMethod.kt, add a parameter for the search query and update the TextField accordingly:*/
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTextMethod(searchQuery: String, onSearchQueryChange: (String) -> Unit) {

    TextField(
        value = searchQuery,
        onValueChange = onSearchQueryChange,
        placeholder = { Text("Search...") },
        modifier = Modifier
            .fillMaxWidth(),
        textStyle = TextStyle.Default.copy(fontSize = 18.sp)

    )
}
