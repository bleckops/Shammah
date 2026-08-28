package co.bleck.shammah.composeapp.ui.home

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import co.bleck.shammah.composeapp.ui.auth.AuthViewModel
import co.bleck.shammah.composeapp.ui.components.AdaptiveLayout
import co.bleck.shammah.composeapp.ui.components.AdaptiveMetrics
import co.bleck.shammah.composeapp.ui.components.BottomNavItem
import co.bleck.shammah.composeapp.ui.components.EVENT_DETAIL_ROUTE
import co.bleck.shammah.composeapp.ui.components.SERMON_DETAIL_ROUTE
import co.bleck.shammah.composeapp.ui.components.RESOURCE_DETAIL_ROUTE
import co.bleck.shammah.composeapp.ui.components.adaptiveContentWidth
import co.bleck.shammah.composeapp.ui.home.events.EventsScreen
import co.bleck.shammah.composeapp.ui.home.events.detail.EventDetailScreen
import co.bleck.shammah.composeapp.ui.home.resources.ResourcesScreen
import co.bleck.shammah.composeapp.ui.home.resources.detail.ResourceDetailScreen
import co.bleck.shammah.composeapp.ui.home.sermons.SermonsScreen
import co.bleck.shammah.composeapp.ui.home.sermons.detail.SermonDetailScreen

@Composable
fun MainScreen(
    authViewModel: AuthViewModel,
    onOpenUrl: (String) -> Unit,
    onOpenMaps: (Double, Double, String) -> Unit,
    onShareIcsCalendar: (String, String) -> Unit,
    onShowMessage: (String) -> Unit,
    appVersionName: String,
) {
    val navController = rememberNavController()
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Sermons,
        BottomNavItem.Events,
        BottomNavItem.About,
        BottomNavItem.Resources
    )
    val currentRoute = currentRoute(navController)
    val onNavigate: (String) -> Unit = { route ->
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    AdaptiveLayout { metrics ->
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            if (metrics.useNavigationRail) {
                WideMainShell(
                    metrics = metrics,
                    items = items,
                    currentRoute = currentRoute,
                    onNavigate = onNavigate,
                    navController = navController,
                    authViewModel = authViewModel,
                    onOpenUrl = onOpenUrl,
                    onOpenMaps = onOpenMaps,
                    onShareIcsCalendar = onShareIcsCalendar,
                    onShowMessage = onShowMessage,
                    appVersionName = appVersionName
                )
            } else {
                CompactMainShell(
                    metrics = metrics,
                    items = items,
                    currentRoute = currentRoute,
                    onNavigate = onNavigate,
                    navController = navController,
                    authViewModel = authViewModel,
                    onOpenUrl = onOpenUrl,
                    onOpenMaps = onOpenMaps,
                    onShareIcsCalendar = onShareIcsCalendar,
                    onShowMessage = onShowMessage,
                    appVersionName = appVersionName
                )
            }
        }
    }
}

@Composable
private fun WideMainShell(
    metrics: AdaptiveMetrics,
    items: List<BottomNavItem>,
    currentRoute: String?,
    onNavigate: (String) -> Unit,
    navController: NavHostController,
    authViewModel: AuthViewModel,
    onOpenUrl: (String) -> Unit,
    onOpenMaps: (Double, Double, String) -> Unit,
    onShareIcsCalendar: (String, String) -> Unit,
    onShowMessage: (String) -> Unit,
    appVersionName: String,
) {
    Row(modifier = Modifier.fillMaxSize()) {
        SideNavigationRail(
            items = items,
            currentRoute = currentRoute,
            onNavigate = onNavigate
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.28f)),
            contentAlignment = Alignment.TopCenter
        ) {
            Surface(
                modifier = Modifier
                    .adaptiveContentWidth(metrics)
                    .fillMaxHeight()
                    .fillMaxWidth(),
                color = MaterialTheme.colorScheme.background,
                tonalElevation = 0.dp
            ) {
                AppNavHost(
                    navController = navController,
                    authViewModel = authViewModel,
                    onOpenUrl = onOpenUrl,
                    onOpenMaps = onOpenMaps,
                    onShareIcsCalendar = onShareIcsCalendar,
                    onShowMessage = onShowMessage,
                    appVersionName = appVersionName,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
private fun CompactMainShell(
    metrics: AdaptiveMetrics,
    items: List<BottomNavItem>,
    currentRoute: String?,
    onNavigate: (String) -> Unit,
    navController: NavHostController,
    authViewModel: AuthViewModel,
    onOpenUrl: (String) -> Unit,
    onOpenMaps: (Double, Double, String) -> Unit,
    onShareIcsCalendar: (String, String) -> Unit,
    onShowMessage: (String) -> Unit,
    appVersionName: String,
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                ModernBottomBar(
                    items = items,
                    currentRoute = currentRoute,
                    onNavigate = onNavigate,
                    modifier = Modifier
                        .widthIn(max = if (metrics.isWide) 520.dp else 720.dp)
                        .fillMaxWidth()
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(
                    if (metrics.isWide) {
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.22f)
                    } else {
                        MaterialTheme.colorScheme.background
                    }
                ),
            contentAlignment = Alignment.TopCenter
        ) {
            Box(
                modifier = Modifier
                    .adaptiveContentWidth(metrics)
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                AppNavHost(
                    navController = navController,
                    authViewModel = authViewModel,
                    onOpenUrl = onOpenUrl,
                    onOpenMaps = onOpenMaps,
                    onShareIcsCalendar = onShareIcsCalendar,
                    onShowMessage = onShowMessage,
                    appVersionName = appVersionName,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
private fun AppNavHost(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    onOpenUrl: (String) -> Unit,
    onOpenMaps: (Double, Double, String) -> Unit,
    onShareIcsCalendar: (String, String) -> Unit,
    onShowMessage: (String) -> Unit,
    appVersionName: String,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Home.route,
        modifier = modifier,
        enterTransition = { fadeIn(tween(240)) },
        exitTransition = { fadeOut(tween(180)) },
        popEnterTransition = { fadeIn(tween(240)) },
        popExitTransition = { fadeOut(tween(180)) }
    ) {
        composable(BottomNavItem.Home.route) {
            HomeScreen(
                onOpenUrl = onOpenUrl,
                onOpenMaps = onOpenMaps
            )
        }
        composable(BottomNavItem.Sermons.route) { SermonsScreen(navController) }
        composable(BottomNavItem.Events.route) {
            EventsScreen(
                navController = navController,
                onShareIcsCalendar = onShareIcsCalendar,
                onShowMessage = onShowMessage
            )
        }
        composable(BottomNavItem.About.route) {
            AboutScreen(
                authViewModel = authViewModel,
                appVersionName = appVersionName
            )
        }
        composable(BottomNavItem.Resources.route) { ResourcesScreen(navController) }

        composable(
            route = RESOURCE_DETAIL_ROUTE,
            arguments = listOf(navArgument("resourceId") { type = NavType.StringType }),
            enterTransition = { fadeIn(tween(300)) + slideInHorizontally(tween(300)) { it / 4 } },
            exitTransition = { fadeOut(tween(250)) + slideOutHorizontally(tween(250)) { -it / 4 } },
            popEnterTransition = { fadeIn(tween(300)) + slideInHorizontally(tween(300)) { -it / 4 } },
            popExitTransition = { fadeOut(tween(250)) + slideOutHorizontally(tween(250)) { it / 4 } }
        ) { backStackEntry ->
            key(backStackEntry.id) { ResourceDetailScreen(navController, backStackEntry, onOpenUrl) }
        }

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

        composable(
            route = EVENT_DETAIL_ROUTE,
            arguments = listOf(
                navArgument("eventId") { type = NavType.StringType }
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
                EventDetailScreen(navController, backStackEntry)
            }
        }
    }
}

@Composable
fun currentRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}

@Composable
private fun SideNavigationRail(
    items: List<BottomNavItem>,
    currentRoute: String?,
    onNavigate: (String) -> Unit
) {
    NavigationRail(
        modifier = Modifier
            .fillMaxHeight()
            .width(92.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        header = {
            Column(
                modifier = Modifier
                    .padding(top = 20.dp, bottom = 12.dp)
                    .padding(horizontal = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "Shammah",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    ) {
        items.forEach { item ->
            val selected = currentRoute == item.route
            NavigationRailItem(
                selected = selected,
                onClick = { onNavigate(item.route) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.labelSmall,
                        maxLines = 1
                    )
                },
                colors = NavigationRailItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.secondaryContainer,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}

@Composable
private fun ModernBottomBar(
    items: List<BottomNavItem>,
    currentRoute: String?,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .padding(horizontal = 14.dp, vertical = 8.dp)
            .navigationBarsPadding(),
        shape = RoundedCornerShape(28.dp),
        tonalElevation = 10.dp,
        shadowElevation = 8.dp,
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.96f)
    ) {
        NavigationBar(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = 6.dp),
            containerColor = Color.Transparent,
            tonalElevation = 0.dp
        ) {
            items.forEach { item ->
                val selected = currentRoute == item.route
                NavigationBarItem(
                    selected = selected,
                    onClick = { onNavigate(item.route) },
                    alwaysShowLabel = false,
                    icon = {
                        ModernBottomBarItem(
                            title = item.title,
                            icon = { tint ->
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.title,
                                    tint = tint
                                )
                            },
                            selected = selected
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Color.Transparent,
                        selectedIconColor = Color.Unspecified,
                        unselectedIconColor = Color.Unspecified,
                        selectedTextColor = Color.Unspecified,
                        unselectedTextColor = Color.Unspecified
                    )
                )
            }
        }
    }
}

@Composable
private fun ModernBottomBarItem(
    title: String,
    selected: Boolean,
    icon: @Composable (Color) -> Unit
) {
    val iconTint by animateColorAsState(
        targetValue = if (selected) {
            MaterialTheme.colorScheme.onSecondaryContainer
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        },
        animationSpec = tween(230),
        label = "bottom_bar_icon_tint"
    )
    val labelColor by animateColorAsState(
        targetValue = if (selected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        },
        animationSpec = tween(230),
        label = "bottom_bar_label_tint"
    )
    val iconScale by animateFloatAsState(
        targetValue = if (selected) 1.08f else 1f,
        animationSpec = spring(dampingRatio = 0.62f, stiffness = 520f),
        label = "bottom_bar_icon_scale"
    )
    val iconLift by animateDpAsState(
        targetValue = if (selected) (-2).dp else 0.dp,
        animationSpec = spring(dampingRatio = 0.65f, stiffness = 700f),
        label = "bottom_bar_icon_lift"
    )
    val indicatorColor by animateColorAsState(
        targetValue = if (selected) {
            MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.95f)
        } else {
            Color.Transparent
        },
        animationSpec = tween(260),
        label = "bottom_bar_indicator"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Surface(
            shape = RoundedCornerShape(18.dp),
            color = indicatorColor,
            contentColor = iconTint
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(18.dp))
                    .scale(iconScale)
                    .offset(y = iconLift)
                    .padding(horizontal = 14.dp, vertical = 5.dp),
                contentAlignment = Alignment.Center
            ) {
                icon(iconTint)
            }
        }

        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall,
            color = labelColor
        )
    }
}
