package com.example.breakingnewsapp.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import com.example.breakingnewsapp.models.Article
import com.example.breakingnewsapp.models.ArticleViewModel

/**Add the destination screens, These will be simple functions that do nothing more than display the icon
for the corresponding bar item selection. We will declare each screen composable in a separate file, each of which will
be placed in a new package named screens.*/


@Composable
fun SavedNews(viewModel: ArticleViewModel) {

    val articles = remember { mutableStateOf<List<Article>>(emptyList()) }

    viewModel.savedArticles.observeForever {
        articles.value = it ?: emptyList()
    }

    LazyColumn {
        items(articles.value) { article ->

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
}