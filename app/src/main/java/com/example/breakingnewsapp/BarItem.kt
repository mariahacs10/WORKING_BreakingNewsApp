package com.example.breakingnewsapp

import androidx.compose.ui.graphics.vector.ImageVector


/**Each item in the bottom bar will need a title string, an icon image, and the
route to which the app should navigate when the item is clicked.
we will also declare the bar item class as a separate file
 */
data class BarItem(
    val title: String,
    val image: ImageVector,
    val route: String
)