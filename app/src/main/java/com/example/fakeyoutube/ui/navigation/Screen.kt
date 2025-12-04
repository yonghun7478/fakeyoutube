package com.example.fakeyoutube.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Subscriptions
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screen("home", "Home", Icons.Filled.Home)
    object Shorts : Screen("shorts", "Shorts", Icons.Filled.PlayArrow)
    object Upload : Screen("upload", "Upload", Icons.Filled.AddCircle)
    object Subscription : Screen("subscription", "Subscription", Icons.Filled.Subscriptions)
    object Library : Screen("library", "You", Icons.Filled.AccountCircle)
}