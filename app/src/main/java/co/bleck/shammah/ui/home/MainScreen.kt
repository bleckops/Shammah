package co.bleck.shammah.ui.home

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import co.bleck.shammah.ui.components.BottomNavItem
import co.bleck.shammah.ui.components.SERMON_DETAIL_ROUTE
import co.bleck.shammah.ui.home.events.EventsScreen
import co.bleck.shammah.ui.home.sermons.SermonsScreen
import co.bleck.shammah.ui.home.sermons.detail.SermonDetailScreen

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Sermons,
        BottomNavItem.Events,
        BottomNavItem.About
    )

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = androidx.compose.ui.unit.Dp(3f)
            ) {
                val currentRoute = currentRoute(navController)
                items.forEach { item ->
                    val selected = currentRoute == item.route
                    NavigationBarItem(
                        icon    = { Icon(item.icon, contentDescription = item.title) },
                        label   = { Text(item.title, style = MaterialTheme.typography.labelSmall) },
                        selected = selected,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState    = true
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor   = MaterialTheme.colorScheme.onSecondaryContainer,
                            selectedTextColor   = MaterialTheme.colorScheme.secondary,
                            indicatorColor      = MaterialTheme.colorScheme.secondaryContainer,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController    = navController,
            startDestination = BottomNavItem.Home.route,
            modifier         = Modifier.padding(innerPadding),
            // Bottom nav tabs: crossfade only — no slide feels more natural
            enterTransition  = { fadeIn(tween(240)) },
            exitTransition   = { fadeOut(tween(180)) },
            popEnterTransition  = { fadeIn(tween(240)) },
            popExitTransition   = { fadeOut(tween(180)) }
        ) {
            composable(BottomNavItem.Home.route)    { HomeScreen() }
            composable(BottomNavItem.Sermons.route) { SermonsScreen(navController) }
            composable(BottomNavItem.Events.route)  { EventsScreen() }
            composable(BottomNavItem.About.route)   { AboutScreen() }

            // Sermon detail: slide-in from right (hierarchical navigation)
            composable(
                route = SERMON_DETAIL_ROUTE,
                arguments = listOf(
                    navArgument("sermonId") { type = NavType.StringType }
                ),
                enterTransition = {
                    fadeIn(tween(300)) + slideInHorizontally(tween(300)) { it / 4 }
                },
                exitTransition = {
                    fadeOut(tween(250)) + slideOutHorizontally(tween(250)) { -it / 4 }
                },
                popEnterTransition = {
                    fadeIn(tween(300)) + slideInHorizontally(tween(300)) { -it / 4 }
                },
                popExitTransition = {
                    fadeOut(tween(250)) + slideOutHorizontally(tween(250)) { it / 4 }
                }
            ) { backStackEntry ->
                key(backStackEntry.id) {
                    SermonDetailScreen(navController, backStackEntry)
                }
            }
        }
    }
}

@Composable
fun currentRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}