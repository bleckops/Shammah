package co.bleck.shammah.ui.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.core.tween
import co.bleck.shammah.ui.components.BottomNavItem
import co.bleck.shammah.ui.home.sermons.SermonsScreen
import co.bleck.shammah.ui.home.sermons.detail.SermonDetailScreen

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Sermons,
        BottomNavItem.About
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val currentRoute = currentRoute(navController)
                items.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) },
                        selected = currentRoute == item.route,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(innerPadding),
            enterTransition = {
                fadeIn(animationSpec = tween(300)) + slideInHorizontally(
                    animationSpec = tween(300),
                    initialOffsetX = { x -> x / 4 }
                )
            },
            exitTransition = {
                fadeOut(animationSpec = tween(300)) + slideOutHorizontally(
                    animationSpec = tween(300),
                    targetOffsetX = { x -> -x / 4 }
                )
            },
            popEnterTransition = {
                fadeIn(animationSpec = tween(300)) + slideInHorizontally(
                    animationSpec = tween(300),
                    initialOffsetX = { x -> -x / 4 }
                )
            },
            popExitTransition = {
                fadeOut(animationSpec = tween(300)) + slideOutHorizontally(
                    animationSpec = tween(300),
                    targetOffsetX = { x -> x / 4 }
                )
            }
        ) {
            composable(BottomNavItem.Home.route) { HomeScreen() }
            composable(BottomNavItem.Sermons.route) { SermonsScreen(navController) }
            composable(BottomNavItem.About.route) { AboutScreen() }

            composable("sermon_detail/{sermonId}") { backStackEntry ->
                val sermonId = backStackEntry.arguments?.getString("sermonId") ?: ""
                SermonDetailScreen(sermonId, navController)
            }
        }
    }
}

@Composable
fun currentRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}