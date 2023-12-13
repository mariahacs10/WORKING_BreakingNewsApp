package com.example.breakingnewsapp


/**Create your three navigation routes, which will be declared using a sealed class

(What are NavRoutes?:  it will include a bottom bar containing three
items which, when clicked, will navigate to different screens,)

 */
sealed class NavRoutes(val route: String) {
    object BreakingNews : NavRoutes("Breaking News")
    object SavedNews : NavRoutes("Saved News")
    object SearchNews : NavRoutes("Search News")

}