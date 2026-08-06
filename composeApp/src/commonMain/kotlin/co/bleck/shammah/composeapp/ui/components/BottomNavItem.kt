package co.bleck.shammah.composeapp.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Church
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val title: String, val route: String, val icon: ImageVector) {
    object Home    : BottomNavItem("Inicio",    "home",    Icons.Filled.Home)
    object Sermons : BottomNavItem("Sermones",  "sermons", Icons.AutoMirrored.Filled.MenuBook)
    object Events  : BottomNavItem("Eventos",   "events",  Icons.Filled.CalendarMonth)
    object About   : BottomNavItem("Acerca de", "about",   Icons.Filled.Church)
}

const val SERMON_DETAIL_ROUTE = "sermon_detail/{sermonId}"
