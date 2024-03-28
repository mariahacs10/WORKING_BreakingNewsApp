package com.example.breakingnewsapp.screens


import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.R
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter.State.Empty.painter
import coil.compose.rememberImagePainter
import com.example.breakingnewsapp.api.RetrofitInstanceClass

import com.example.breakingnewsapp.models.Article
import com.example.breakingnewsapp.models.ArticleViewModel

/**Add the destination screens, These will be simple functions that do nothing more than display the icon
for the corresponding bar item selection. We will declare each screen composable in a separate file, each of which will
be placed in a new package named screens.*/


/**1.This function navigates to the full article screen when an article is clicked. */
@Composable
fun AppNavigator(viewModel: ArticleViewModel) {
    // Creates a NavController to handle navigation
    val navController = rememberNavController()

    // NavHost manages navigation between different destinations
    NavHost(navController, startDestination = "articleList") {
        // Destination to show list of articles
        composable("articleList") {
            BreakingNews(navController, viewModel)
        }

        // Destination to show full article
        composable("fullArticle") {
            FullArticleScreen(viewModel)
        }
    }
}

// Shows full article using WebView
@Composable
fun FullArticleScreen(viewModel: ArticleViewModel) {
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
        val context = LocalContext.current

        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 78.dp),
          //This makes the heart clickable
            //and saved to the database
            onClick = {  article?.let {
                viewModel.saveArticle(it)
                Toast.makeText(context, "Article Saved", Toast.LENGTH_SHORT).show()
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
fun BreakingNewsCard(
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


/**5. This @Composable function displays an image using the Coil library.*/
@OptIn(ExperimentalCoilApi::class)
@Composable
fun CoilImage(imageUrl: String, contentDescription: String?, modifier: Modifier = Modifier) {

    //This checks the imageUrl if its empty if its empty
    val imageModel = if(imageUrl.isEmpty()) {
        //You need to add the placeholder, and this will show if theres a article with no image
        painterResource(com.example.breakingnewsapp.R.drawable.noimagefound)
    }
    //else if the image has a image and it works just load the
    // image from URL with Coil
    else {
        rememberImagePainter(imageUrl)
    }

    //This is how the image geets displayed
    Image(
        painter = imageModel,
        contentDescription = contentDescription,
        modifier = modifier
    )
}

/**6. This @Composable function displays a list of news articles. It uses a LazyColumn to display a list of BreakingNewsCard*/
@Composable
fun BreakingNewsList(navController: NavController, articles: List<Article>, viewModel: ArticleViewModel) {
    // LazyColumn displays list of cards
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(articles) { article ->
            BreakingNewsCard(navController, article, viewModel)
        }
    }
}


/**7. This @Composable function fetches the breaking n/ews articles and displays them using the BreakingNewsList composable*/
@Composable
fun BreakingNews(navController: NavController, viewModel: ArticleViewModel) {

    val articles = remember { mutableStateOf<List<Article>>(emptyList()) }

    // Fetch articles
    LaunchedEffect(Unit) {
        val response = RetrofitInstanceClass.api.getBreakingNews()
        articles.value = response.body()?.articles ?: emptyList()
    }

    // Display list of articles
    BreakingNewsList(navController, articles.value, viewModel)
}