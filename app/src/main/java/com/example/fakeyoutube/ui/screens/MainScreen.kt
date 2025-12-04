package com.example.fakeyoutube.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.fakeyoutube.ui.components.BottomNavigationBar
import com.example.fakeyoutube.ui.components.HomeTopBar
import com.example.fakeyoutube.ui.navigation.Screen

import androidx.compose.material3.ExperimentalMaterial3Api

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        topBar = {
            // Show HomeTopBar only when on the Home screen
            if (currentRoute == Screen.Home.route) {
                HomeTopBar()
            }
        },
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen()
            }
            composable(Screen.Shorts.route) {
                ShortsScreen()
            }
            composable(Screen.Upload.route) {
                UploadScreen()
            }
            composable(Screen.Subscription.route) {
                SubscriptionScreen()
            }
            composable(Screen.Library.route) {
                LibraryScreen()
            }
        }
    }
}
