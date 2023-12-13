package com.example.breakingnewsapp


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.breakingnewsapp.models.Article
import com.example.breakingnewsapp.models.ArticleViewModel
import com.example.breakingnewsapp.screens.AppNavigator
import com.example.breakingnewsapp.screens.SavedNews
import com.example.breakingnewsapp.screens.SearchNews



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
}

/** Working with the Scaffold component
The final task before testing the project is to complete the layout in the
MainScreen function. For this, we will use the Compose Scaffold.

(What is Scaffold?:  layout structure for organizing UI elements on the screen
responsively)
 */


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(

        content = { padding ->
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(56.dp)) {
                NavigationHost(navController = navController)
            } },
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    )

}

@Composable
fun NavigationHost(navController: NavHostController)
{
    NavHost(navController = navController, startDestination = NavRoutes.BreakingNews.route) {
        composable(NavRoutes.BreakingNews.route) {
            // Create an instance of your ViewModel
            val viewModel: ArticleViewModel = ViewModelProvider(it).get(ArticleViewModel::class.java)
            // Create a NavHostController
            // Use your AppNavigator composable
            AppNavigator(viewModel)
        }

        composable(NavRoutes.SavedNews.route) {
            SavedNews()
        }
        composable(NavRoutes.SearchNews.route) {
            SearchNews()
        }
    }
}


@Composable
fun BottomNavigationBar(navController: NavHostController){
    NavigationBar {
        val backStackEntry by navController.currentBackStackEntryAsState()

        val currentRoute =
            backStackEntry?.destination?.route

        NavBarItems.BarItems.forEach { navItem ->
            NavigationBarItem(
                selected = currentRoute ==
                        navItem.route,
                onClick = {
                    navController.navigate(navItem.
                    route) {
                        popUpTo(navController.graph.
                        findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(imageVector =
                    navItem.image,
                        contentDescription =
                        navItem.title)
                },
                label = {
                    Text(text = navItem.title)
                },
            )
        }
    }
}