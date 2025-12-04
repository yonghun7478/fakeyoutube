package com.example.fakeyoutube.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fakeyoutube.ui.components.BottomNavigationBar
import com.example.fakeyoutube.ui.navigation.Screen
import com.example.fakeyoutube.ui.screens.HomeScreen
import com.example.fakeyoutube.ui.screens.LibraryScreen
import com.example.fakeyoutube.ui.screens.ShortsScreen
import com.example.fakeyoutube.ui.screens.SubscriptionScreen
import com.example.fakeyoutube.ui.screens.UploadScreen

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) { HomeScreen() }
            composable(Screen.Shorts.route) { ShortsScreen() }
            composable(Screen.Upload.route) { UploadScreen() }
            composable(Screen.Subscription.route) { SubscriptionScreen() }
            composable(Screen.Library.route) { LibraryScreen() }
        }
    }
}