package com.example.breakingnewsapp.screens

import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.breakingnewsapp.models.Article
import com.example.breakingnewsapp.models.ArticleViewModel

/**Add the destination screens, These will be simple functions that do nothing more than display the icon
for the corresponding bar item selection. We will declare each screen composable in a separate file, each of which will
be placed in a new package named screens.*/

@Composable
fun AppNavigator3(viewModel: ArticleViewModel) {
    // Creates a NavController to handle navigation
    val navController = rememberNavController()

    // NavHost manages navigation between different destinations
    NavHost(navController, startDestination = "articleList") {
        // Destination to show list of articles
        composable("articleList") {
            SavedNews(viewModel, navController)
        }

        // Destination to show full article
        composable("fullArticle") {
            FullArticleScreenSaved(viewModel)
        }
    }
}


// This Composable function displays the full article using a WebView.
@Composable
fun FullArticleScreenSaved(viewModel: ArticleViewModel) {
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
    }
}



// Card to display single article
@Composable
fun SavedNewsCard(
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
fun SavedNewsList(navController: NavController, articles: List<Article>, viewModel: ArticleViewModel) {
    // LazyColumn displays list of cards
    LazyColumn {
        items(articles) { article ->
            SavedNewsCard(navController, article, viewModel)
        }
    }
}


@Composable
fun SavedNews(viewModel: ArticleViewModel,navController: NavController) {

    val articles = remember { mutableStateOf<List<Article>>(emptyList()) }

    viewModel.savedArticles.observeForever {
        articles.value = it ?: emptyList()
    }

    SavedNewsList(navController, articles.value, viewModel)
}