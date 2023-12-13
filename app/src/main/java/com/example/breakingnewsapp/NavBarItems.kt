package com.example.breakingnewsapp

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.materialIcon

/**Next step is to create a list containing the three bar items, each configured with the
apporiate string, image and route properties. */
object NavBarItems {
    val BarItems = listOf(
        BarItem(
            title = "Breaking News",
            image = Icons.Filled.Warning,
            route = "Breaking News"
        ),
        BarItem(
            title = "Saved News",
            image = Icons.Filled.Favorite,
            route = "Saved News"
        ),
        BarItem(
            title = "Search News",
            image = Icons.Filled.Menu,
            route = "Search News"
        )
    )
}