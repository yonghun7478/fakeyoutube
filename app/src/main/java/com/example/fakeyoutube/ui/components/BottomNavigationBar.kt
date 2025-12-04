package com.example.fakeyoutube.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Subscriptions
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Subscriptions
import androidx.compose.material.icons.outlined.VideoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

// Navigation Item Definition
sealed class BottomNavItem(
    val route: String,
    val label: String,
    val iconFilled: ImageVector,
    val iconOutlined: ImageVector,
    val isCreateButton: Boolean = false,
    val isProfile: Boolean = false
) {
    object Home : BottomNavItem("home", "홈", Icons.Filled.Home, Icons.Outlined.Home)
    // Using VideoLibrary as placeholder for Shorts icon
    object Shorts : BottomNavItem("shorts", "쇼츠", Icons.Filled.VideoLibrary, Icons.Outlined.VideoLibrary)
    object Create : BottomNavItem("create", "", Icons.Filled.Add, Icons.Filled.Add, isCreateButton = true)
    object Subscriptions : BottomNavItem("subscriptions", "구독", Icons.Filled.Subscriptions, Icons.Outlined.Subscriptions)
    object MyPage : BottomNavItem("mypage", "마이페이지", Icons.Filled.AccountCircle, Icons.Filled.AccountCircle, isProfile = true)
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Shorts,
        BottomNavItem.Create,
        BottomNavItem.Subscriptions,
        BottomNavItem.MyPage
    )

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        val navBackStackEntry = navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry.value?.destination?.route

        items.forEach { item ->
            val selected = currentRoute == item.route

            NavigationBarItem(
                selected = selected,
                onClick = {
                    if (item.route.isNotEmpty()) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    }
                },
                icon = {
                    if (item.isCreateButton) {
                        // Custom 'Create' (+) Button Style
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color.LightGray.copy(alpha = 0.5f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = item.iconFilled,
                                contentDescription = "Create",
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    } else if (item.isProfile) {
                        // Profile Placeholder
                        Icon(
                            imageVector = item.iconFilled,
                            contentDescription = item.label,
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                        )
                    } else {
                        Icon(
                            imageVector = if (selected) item.iconFilled else item.iconOutlined,
                            contentDescription = item.label
                        )
                    }
                },
                label = {
                    if (!item.isCreateButton) {
                        Text(
                            text = item.label,
                            fontSize = 10.sp
                        )
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent,
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}