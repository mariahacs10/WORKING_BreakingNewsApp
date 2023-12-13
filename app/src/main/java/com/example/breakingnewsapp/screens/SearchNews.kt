package com.example.breakingnewsapp.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.breakingnewsapp.api.RetrofitInstanceClass
import com.example.breakingnewsapp.models.Article
import com.example.breakingnewsapp.models.ArticleViewModel

/**Add the destination screens, These will be simple functions that do nothing more than display the icon
for the corresponding bar item selection. We will declare each screen composable in a separate file, each of which will
be placed in a new package named screens.*/

@Composable
fun SearchNews(
) {
    /**In SearchNews.kt, add a function to handle the search query:*/
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
            onSearchQueryChange = { searchQuery = it },)
        /**you wanna call BreakingnewsList because your still getting the breaking news your just searching for
         * it and you'll get even more news*/
      // BreakingNewsList(articles.value)
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
